package paydemo.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.*;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.dao.dbmodel.PayFundDO;
import paydemo.dao.dbmodel.PayJobDetailDO;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.facade.mq.PayNoticeMessage;
import paydemo.manager.db.PayOrderManager;
import paydemo.manager.helper.ChannelDetailAccumulateHelper;
import paydemo.manager.helper.PaySeqNoCreator;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.model.PayRequestBO;
import paydemo.manager.model.PayResponseBO;
import paydemo.manager.mq.rocketmq.MessageProducer;
import paydemo.util.FlagsEnum;
import paydemo.util.JsonUtil;
import paydemo.util.PayStatusEnum;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @auther YDXiaa
 */
@Slf4j
@Service
public class PayBiz extends AbstractPayBiz<PayRequestBO> {

    @Autowired
    private PayOrderManager payOrderManager;

    @Autowired
    private PaySeqNoCreator paySeqNoCreator;

    @Autowired
    private PayProcessorFactory processorFactory;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private ChannelDetailAccumulateHelper accumulateHelper;

    /**
     * 支付前置处理.
     *
     * @param request       支付请求对象.
     * @param payResponseBO 支付统一响应对象.
     */
    @Override
    protected void beforePayProcess(PayRequestBO request, PayResponseBO payResponseBO) {

        // 支付订单状态推进不落库.
        if (request.isContinueCompletePayOrder()) {
            return;
        }

        // 订单唯一性校验.
        verifyUniqueOrder(request);

        // 组装支付单、支付资金单.
        buildPayFund(request);

        // 落单.
        addPayOrder(request);
    }

    /**
     * 插入支付单.
     *
     * @param request 支付请求对象.
     */
    private void addPayOrder(PayRequestBO request) {

        PayOrderDO payOrderDO = BeanUtil.copyProperties(request, PayOrderDO.class);
        List<PayFundDO> payFundDOList = BeanUtil.copyToList(request.getPayFundBOList(), PayFundDO.class);

        payOrderManager.addPayOrder(payOrderDO, payFundDOList);
    }

    /**
     * 组装支付单、支付资金单.
     *
     * @param request 请求对象.
     */
    private void buildPayFund(PayRequestBO request) {

        // 设置支付单号.
        request.setPayNo(paySeqNoCreator.createPaySeqNo(request.getRequestDate(), request.getBizLine(),
                request.getPayType(), RedisKeyEnum.PAYCORE_PAY));
        request.setPayCreateDate(new Date());
        request.setRefundAmt(0L);
        request.setPayStatus(PayStatusEnum.INIT.getStatusCode());

        List<PayFundBO> payFundBOList = request.getPayFundBOList();

        // 按照支付顺序排序执行顺序.
        Collections.sort(payFundBOList);

        // 支付资金单设置信息.
        for (int i = 1; i <= payFundBOList.size(); i++) {
            PayFundBO payFundBO = payFundBOList.get(i - 1);
            payFundBO.setPayFundNo(paySeqNoCreator.createPaySeqNo(request.getRequestDate(), request.getBizLine(),
                    payFundBO.getPayType(), RedisKeyEnum.PAYCORE_PAY_FUND_NO));
            payFundBO.setPayNo(request.getPayNo());
            payFundBO.setPayCreateDate(request.getPayCreateDate());
            payFundBO.setPayStatus(PayStatusEnum.INIT.getStatusCode());
            payFundBO.setPaySeq(i);
            payFundBO.setRefundAmt(0L);
            payFundBO.setRevokeFlag(FlagsEnum.FALSE.getFlagCode());
        }

    }

    /**
     * 订单唯一性校验.
     *
     * @param request 请求对象.
     */
    private void verifyUniqueOrder(PayRequestBO request) {

        PayOrderDO payOrderDO = payOrderManager.queryPayOrder(request.getRequestBizNo(), request.getRequestDate(), request.getBizLine());

        if (Objects.nonNull(payOrderDO)) {
            PayException.throwBizFailException(ResponseCodeEnum.ORDER_SYS_EXISTS);
        }
    }

    /**
     * 支付核心处理.
     * <p>
     * 1、三方渠道订单作为最后一笔订单.
     * 2、执行逻辑：先借后贷、逆向顺序冲正（例如支付顺序为 1,2,3,4 冲正逻辑为 4,3,2,1）
     *
     * @param request       支付请求对象.
     * @param payResponseBO 支付响应对象.
     */
    @Override
    protected void payProcess(PayRequestBO request, PayResponseBO payResponseBO) {

        payResponseBO.setPayNo(request.getPayNo());
        payResponseBO.setPayCreateDate(request.getPayCreateDate());

        // 更新支付单状态为处理中.
        modifyPayOrderStatus2Paying(request);

        int totalPayFundCnt = request.getPayFundBOList().size();

        for (int i = 1; i <= totalPayFundCnt; i++) {

            PayFundBO payFundBO = request.getPayFundBOList().get(i - 1);

            // 执行远程调用,调用各个资金系统.
            RemotePayResult remotePayResult = request.getLastRemotePayResult();
            if (null == remotePayResult) {
                remotePayResult = doRemotePayProcess(payFundBO);
            }

            payResponseBO.setPayStatus(remotePayResult.getPayStatus());
            payResponseBO.setResultCode(remotePayResult.getResultCode());
            payResponseBO.setResultDesc(remotePayResult.getResultDesc());
            payResponseBO.setChannelDetailNo(remotePayResult.getChannelDetailNo());

            // 判断执行结果，如果支付状态为支付中、支付失败，需要冲正支付资金单.
            if (isExecRevoke(remotePayResult, i, totalPayFundCnt, request.isContinueCompletePayOrder())) {

                log.info("执行冲正逻辑,当前支付资金单序号:{},总支付资金单数量:{}", i, totalPayFundCnt);
                doPayRevokeProcess(i, request, payResponseBO);
                break;
            }
        }
    }

    /**
     * 更新支付单状态处理中.
     *
     * @param request 支付请求对象.
     */
    private void modifyPayOrderStatus2Paying(PayRequestBO request) {

        if (!request.isContinueCompletePayOrder()) {
            // 状态处理.
            PayOrderDO payOrderDO = new PayOrderDO();
            payOrderDO.setPayNo(request.getPayNo());
            payOrderDO.setPayStatus(PayStatusEnum.PAYING.getStatusCode());

            int modifyCnt = payOrderManager.modifyPayOrderStatus(payOrderDO, PayStatusEnum.INIT);
            VerifyUtil.verifySqlResult(modifyCnt);
        }

    }

    /**
     * 冲正逻辑.
     *
     * @param seq           当前序号.
     * @param request       请求对象.
     * @param payResponseBO 支付响应对象.
     */
    public void doPayRevokeProcess(int seq, PayRequestBO request, PayResponseBO payResponseBO) {

        // 当前这笔为失败不用加入到冲正单中.
        boolean unAddRevokeFlag = PayStatusEnum.FAIL.getStatusCode().equals(payResponseBO.getPayStatus());

        // 支付单设置为失败.
        payResponseBO.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
        payResponseBO.setPayCompletedDate(new Date());
        payResponseBO.setRevokeFlag(true);

        List<PayFundBO> payFundBOList = request.getPayFundBOList();

        // 剩余未支付的单子需要推定到CLOSE状态.
        List<PayFundBO> toClosePayFundList = Lists.newArrayList();
        // 已经支付的单子需要冲正.
        List<PayFundBO> toRevokePayFundList = Lists.newArrayList();

        // 逆向顺序冲正.
        for (int i = payFundBOList.size(); i >= 1; i--) {
            PayFundBO payFundBO = payFundBOList.get(i - 1);
            // 未被执行单子设置为CLOSE.
            if (payFundBO.getPaySeq() > seq) {
                payFundBO.setPayCompletedDate(request.getPayCompleteDate());
                payFundBO.setPayStatus(PayStatusEnum.CLOSE.getStatusCode());
                toClosePayFundList.add(payFundBO);
            } else {
                // 如果已经是失败状态,不冲正.
                if (!unAddRevokeFlag || seq != i) {
                    toRevokePayFundList.add(payFundBO);
                }
            }
        }

        // 更新数据库状态.
        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setPayNo(request.getPayNo());
        payOrderDO.setPayStatus(payResponseBO.getPayStatus());
        payOrderDO.setPayCompletedDate(payResponseBO.getPayCompletedDate());

        // 冲正单.
        List<PayFundDO> revokePayFundList = createRevokeFund(toRevokePayFundList, request);
        List<PayJobDetailDO> asynRevokePayJobDetailList = createRevokeJobDetail(revokePayFundList);

        // 更新订单状态.
        payOrderManager.modifyRevokePayStatus(payOrderDO, BeanUtil.copyToList(toClosePayFundList, PayFundDO.class),
                revokePayFundList, asynRevokePayJobDetailList);
    }

    /**
     * 冲正资金单.
     *
     * @param toRevokePayFundList 原资金单.
     * @return 冲正资金单.
     */
    private List<PayFundDO> createRevokeFund(List<PayFundBO> toRevokePayFundList, PayRequestBO request) {

        // 冲正单.
        List<PayFundDO> revokeFundList = Lists.newArrayList();

        // 支付资金单设置信息.
        for (int i = 1; i <= toRevokePayFundList.size(); i++) {
            PayFundBO origPayFundBO = toRevokePayFundList.get(i - 1);

            PayFundDO payFundDO = new PayFundDO();
            payFundDO.setPayType(PayTypeEnum.REVOKE.getPayTypeCode());
            payFundDO.setPayTool(origPayFundBO.getPayTool());
            payFundDO.setPaySubTool(origPayFundBO.getPaySubTool());
            payFundDO.setPayFundNo(paySeqNoCreator.createPaySeqNo(request.getRequestDate(), request.getBizLine(),
                    PayTypeEnum.REVOKE.getPayTypeCode(), RedisKeyEnum.PAYCORE_PAY_FUND_NO));
            payFundDO.setPayNo(origPayFundBO.getPayNo());
            payFundDO.setPayAmt(origPayFundBO.getPayAmt());
            payFundDO.setPayCreateDate(new Date());
            payFundDO.setCouponNo(origPayFundBO.getCouponNo());
            payFundDO.setPayerAccClassify(origPayFundBO.getPayerAccClassify());
            payFundDO.setPayerAccType(origPayFundBO.getPayerAccType());
            payFundDO.setPayerAccNo(origPayFundBO.getPayerAccNo());
            // 原资金单号.
            payFundDO.setOrigPayFundNo(origPayFundBO.getPayFundNo());
            payFundDO.setPayStatus(PayStatusEnum.INIT.getStatusCode());
            payFundDO.setPaySeq(i);
            payFundDO.setRefundAmt(0L);
            payFundDO.setRevokeFlag(FlagsEnum.FALSE.getFlagCode());

            revokeFundList.add(payFundDO);
        }

        return revokeFundList;
    }

    /**
     * 支付冲正异步任务作业单.
     *
     * @param toRevokePayFundList 支付冲正任务单.
     * @return 异步作业单.
     */
    private List<PayJobDetailDO> createRevokeJobDetail(List<PayFundDO> toRevokePayFundList) {

        return toRevokePayFundList.stream().map(payFundDO -> {

//                PayJobDetailDO.builder()
//                .jobDetailId(payFundDO.getPayFundNo())
//                .jobType(JobTypeEnum.PAY_REVOKE.getJobTypeCode())
//                .jobDetailDesc(JobTypeEnum.PAY_REVOKE.getJobTypeDesc())
//                .failRetryTimes(1L) //执行一次.
//                .execInterval(SysConstant.JOB_DEFAULT_EXECUTE_INTERVAL)
//                .execStatus(ExecStatusEnum.READY.getExecStatusCode())
//                .nextExecTime(new Date())
//                .execTimes(0L)
//                .shardingMark(0L)
//                .build()

            PayJobDetailDO payJobDetailDO = new PayJobDetailDO();

            payJobDetailDO.setJobDetailId(payFundDO.getPayFundNo());
            payJobDetailDO.setJobDetailDesc(JobTypeEnum.PAY_REVOKE.getJobTypeDesc());
            payJobDetailDO.setJobType(JobTypeEnum.PAY_REVOKE.getJobTypeCode());
            payJobDetailDO.setExecInterval(SysConstant.JOB_DEFAULT_EXECUTE_INTERVAL);
            payJobDetailDO.setFailRetryTimes(1L);
            payJobDetailDO.setExecTimes(0L);
            payJobDetailDO.setNextExecTime(new Date());
            payJobDetailDO.setShardingMark(0L);
            payJobDetailDO.setExecStatus(ExecStatusEnum.READY.getExecStatusCode());

            return payJobDetailDO;
        }).collect(Collectors.toList());
    }

    /**
     * 是否执行冲正(非成功状态需要冲正).
     *
     * @param remotePayResult          资金系统处理结果.
     * @param seq                      当前资金单序号.
     * @param totalFundCnt             资金单数量.
     * @param continueCompletePayOrder 继续完成订单，不一定是成功订单.
     * @return 是否执行冲正操作.
     */
    private boolean isExecRevoke(RemotePayResult remotePayResult, int seq, int totalFundCnt, boolean continueCompletePayOrder) {
        // 支付失败, 第一笔失败直接或者总资金单只有一笔.
        if (StrUtil.equals(PayStatusEnum.SUCCESS.getStatusCode(), remotePayResult.getPayStatus())) {
            return false;
        } else if (StrUtil.equals(PayStatusEnum.FAIL.getStatusCode(), remotePayResult.getPayStatus())) {
            return seq != 1 && totalFundCnt != 1;
        } else {
            //  如果为最后一笔,订单不推进直接冲正.
            return seq == totalFundCnt && continueCompletePayOrder;
        }
    }

    /**
     * 执行支付请求.
     *
     * @param payFundBO 支付资金单.
     */
    private RemotePayResult doRemotePayProcess(PayFundBO payFundBO) {
        return processorFactory.getPayProcessor(payFundBO.getPayTool()).pay(payFundBO);
    }

    /**
     * 支付后置处理.
     *
     * @param request       支付统一响应对象.
     * @param payResponseBO 支付统一响应对象.
     */
    @Override
    protected void afterPayProcess(PayRequestBO request, PayResponseBO payResponseBO) {

        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setPayNo(request.getPayNo());

        // 交易成功处理.
        if (PayStatusEnum.SUCCESS.getStatusCode().equals(payResponseBO.getPayStatus())) {

            payOrderDO.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
            payOrderDO.setPayCompletedDate(new Date());
            int modifyCnt = payOrderManager.modifyPayOrderStatus(payOrderDO, PayStatusEnum.PAYING);
            VerifyUtil.verifySqlResult(modifyCnt);

            // todo 支付成功进行账务记账.


            payResponseBO.setPayCompletedDate(payOrderDO.getPayCompletedDate());

        } else if (PayStatusEnum.FAIL.getStatusCode().equals(payResponseBO.getPayStatus())) {
            // 冲正已经更新数据库，所以此处无需再次更新.
            if (!payResponseBO.isRevokeFlag()) {
                payOrderDO.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
                payOrderDO.setPayCompletedDate(new Date());
                int modifyCnt = payOrderManager.modifyPayOrderStatus(payOrderDO, PayStatusEnum.PAYING);
                VerifyUtil.verifySqlResult(modifyCnt);

                payResponseBO.setPayCompletedDate(payOrderDO.getPayCompletedDate());
            }
        }

        //  发送事件消息.
        if (StrUtil.isNotBlank(payResponseBO.getChannelDetailNo())) {
            accumulateHelper.accumulateChannelDetail(payResponseBO.getPayStatus(), payResponseBO.getChannelDetailNo());
        }

        // 发送kafka消息.
        PayNoticeMessage payNoticeMessage = PayNoticeMessage.builder().payNo(payResponseBO.getPayNo())
                .payStatus(payResponseBO.getPayStatus()).build();
        messageProducer.sendRealTimeMessage(JsonUtil.toJsonStr(payNoticeMessage),
                MQTopicEnum.PAY_RESULT_TOPIC.getTopicCode());
    }
}
