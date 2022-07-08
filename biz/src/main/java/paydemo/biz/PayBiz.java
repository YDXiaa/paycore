package paydemo.biz;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.manager.mq.rocketmq.MessageProducer;
import paydemo.manager.helper.ChannelDetailAccumulateHelper;
import paydemo.common.*;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.dao.dbmodel.PayFundDO;
import paydemo.dao.dbmodel.PayJobDetailDO;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.manager.db.PayJobDetailCreator;
import paydemo.manager.db.PayOrderManager;
import paydemo.manager.helper.BeanCopier;
import paydemo.manager.helper.PaySeqNoCreator;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.model.PayRequestBO;
import paydemo.manager.model.PayResponseBO;

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

        PayOrderDO payOrderDO = BeanCopier.objCopy(request, PayOrderDO.class);
        List<PayFundDO> payFundDOList = BeanCopier.objListCopy(request.getPayFundBOList(), PayFundDO.class);

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
            payFundBO.setRevokeFlag(RevokeFlagEnum.FALSE.getRevokeFlagCode());
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

        // 状态处理.
        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setPayNo(request.getPayNo());
        payOrderDO.setPayStatus(PayStatusEnum.PAYING.getStatusCode());

        int modifyCnt = payOrderManager.modifyPayOrderStatus(payOrderDO, PayStatusEnum.INIT);
        VerifyUtil.verifySqlResult(modifyCnt);

        int totalPayFundCnt = request.getPayFundBOList().size();

        for (int i = 1; i <= totalPayFundCnt; i++) {

            PayFundBO payFundBO = request.getPayFundBOList().get(i - 1);

            // 执行远程调用,调用各个资金系统.
            RemotePayResult remotePayResult = doRemotePayProcess(payFundBO);

            payResponseBO.setPayStatus(remotePayResult.getPayStatus());
            payResponseBO.setResultCode(remotePayResult.getResultCode());
            payResponseBO.setResultDesc(remotePayResult.getResultDesc());
            payResponseBO.setChannelDetailNo(remotePayResult.getChannelDetailNo());

            // 判断执行结果，如果支付状态为支付中、支付失败，需要冲正支付资金单.
            if (isExecRevoke(remotePayResult, i, totalPayFundCnt)) {

                log.info("执行冲正逻辑,当前支付资金单序号:{},总支付资金单数量:{}",i,totalPayFundCnt);
                doPayRevokeProcess(i, request, payResponseBO);
                break;
            }
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
        List<PayJobDetailDO> asynRevokePayJobDetailList = toRevokePayFundList.stream().map(payFundBO ->
                PayJobDetailCreator.createPayJobDetailDO(payFundBO.getPayFundNo(), JobTypeEnum.PAY_REVOKE.getJobTypeCode()))
                .collect(Collectors.toList());

        // 更新订单状态.
        payOrderManager.modifyRevokePayStatus(payOrderDO, BeanCopier.objListCopy(toClosePayFundList, PayFundDO.class),
                asynRevokePayJobDetailList);
    }

    /**
     * 是否执行冲正(非成功状态需要冲正).
     *
     * @param remotePayResult 资金系统处理结果.
     * @param seq             当前资金单序号.
     * @param totalFundCnt    资金单数量.
     * @return 是否执行冲正操作.
     */
    private boolean isExecRevoke(RemotePayResult remotePayResult, int seq, int totalFundCnt) {
        // 支付失败, 第一笔失败直接或者总资金单只有一笔.
        if (PayStatusEnum.FAIL.getStatusCode().equals(remotePayResult.getPayStatus())) {
            return seq != 1 && totalFundCnt != 1;
            // 支付中但是不为最后一笔需要冲正.
        } else if (PayStatusEnum.PAYING.getStatusCode().equals(remotePayResult.getPayStatus()) && seq != totalFundCnt) {
            return true;
            // 为最后一笔PAYING不需要
        } else return PayStatusEnum.PAYING.getStatusCode().equals(remotePayResult.getPayStatus());
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
        payOrderDO.setPayCompletedDate(new Date());

        // 交易成功处理.
        if (PayStatusEnum.SUCCESS.getStatusCode().equals(payResponseBO.getPayStatus())) {

            payOrderDO.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
            int modifyCnt = payOrderManager.modifyPayOrderStatus(payOrderDO, PayStatusEnum.PAYING);
            VerifyUtil.verifySqlResult(modifyCnt);

        } else if (PayStatusEnum.FAIL.getStatusCode().equals(payResponseBO.getPayStatus())) {
            // 冲正已经更新数据库，所以此处无需再次更新.
            if (!payResponseBO.isRevokeFlag()) {
                payOrderDO.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
                int modifyCnt = payOrderManager.modifyPayOrderStatus(payOrderDO, PayStatusEnum.PAYING);
                VerifyUtil.verifySqlResult(modifyCnt);
            }
        }

        //  发送事件消息.
        accumulateHelper.accumulateChannelDetail(payResponseBO.getPayStatus(),payResponseBO.getChannelDetailNo());
        // 发送kafka消息.

    }
}
