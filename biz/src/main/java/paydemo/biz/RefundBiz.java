package paydemo.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import paydemo.common.*;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.dao.dbmodel.PayFundDO;
import paydemo.dao.dbmodel.PayJobDetailDO;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.facade.mq.PayNoticeMessage;
import paydemo.manager.db.PayFundManager;
import paydemo.manager.db.PayOrderManager;
import paydemo.manager.helper.PaySeqNoCreator;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.model.PayResponseBO;
import paydemo.manager.model.RefundRequestBO;
import paydemo.manager.mq.rocketmq.MessageProducer;
import paydemo.util.FlagsEnum;
import paydemo.util.JsonUtil;
import paydemo.util.PayStatusEnum;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther YDXiaa
 * <p>
 * 退款biz.
 */
@Service
public class RefundBiz extends AbstractPayBiz<RefundRequestBO> {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PayOrderManager payOrderManager;

    @Autowired
    private PayFundManager payFundManager;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private PaySeqNoCreator paySeqNoCreator;


    @Override
    protected void beforePayProcess(RefundRequestBO request, PayResponseBO payResponseBO) {

        // 退款流程处理.
        refundProcess(request);
    }


    @Override
    protected void payProcess(RefundRequestBO request, PayResponseBO payResponseBO) {

        // 落库成功，直接回执成功，进行异步退款.
        payResponseBO.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
        payResponseBO.setPayNo(request.getPayNo());
        payResponseBO.setPayCreateDate(request.getPayCreateDate());
        payResponseBO.setPayCompletedDate(new Date());
    }

    @Override
    protected void afterPayProcess(RefundRequestBO request, PayResponseBO payResponseBO) {

        // 发送rocketMQ消息.
        PayNoticeMessage payNoticeMessage = PayNoticeMessage.builder().payNo(payResponseBO.getPayNo())
                .payStatus(payResponseBO.getPayStatus())
                .build();
        messageProducer.sendRealTimeMessage(JsonUtil.toJsonStr(payNoticeMessage), MQTopicEnum.PAY_RESULT_TOPIC.getTopicCode());
    }

    /**
     * 退款处理.
     *
     * @param request 请求对象.
     */
    private void refundProcess(RefundRequestBO request) {

        transactionTemplate.execute(transactionStatus -> {

            // 订单锁定.
            PayOrderDO payOrderDO = payOrderManager.queryPayNoWithLock(request.getOrigRequestBizNo(), request.getOrigRequestDate(),
                    request.getBizLine());

            // 退款验证.
            refundVerify(request, payOrderDO);

            // 组件退款资金单.
            buildRefundFund(request, payOrderDO);

            // 落单.
            addRefundOrder(request);

            return true;
        });
    }

    /**
     * 落库支付单.
     *
     * @param request 请求对象.
     */
    private void addRefundOrder(RefundRequestBO request) {

        PayOrderDO refundOrder = createRefundOrder(request);
        List<PayFundDO> origModifyPayFundList = BeanUtil.copyToList(request.getRefundOrigPayFundBOList(), PayFundDO.class);
        List<PayFundDO> refundFundList = BeanUtil.copyToList(request.getRefundFundBOList(), PayFundDO.class);
        List<PayJobDetailDO> refundJobDetailList = createRefundJobDetail(refundFundList);

        // 添加退款单.
        payOrderManager.addRefundOrder(refundOrder, origModifyPayFundList, refundFundList,refundJobDetailList);
    }

    /**
     * 退款主单.
     *
     * @param request 请求对象.
     * @return 退款主单.
     */
    private PayOrderDO createRefundOrder(RefundRequestBO request) {

        PayOrderDO payOrderDO = new PayOrderDO();

        payOrderDO.setRequestBizNo(request.getRequestBizNo());
        payOrderDO.setRequestDate(request.getRequestDate());
        payOrderDO.setBizLine(request.getBizLine());
        payOrderDO.setPayNo(request.getPayNo());
        payOrderDO.setPayAmt(request.getPayAmt());
        payOrderDO.setRefundAmt(0L);
        payOrderDO.setPayType(PayTypeEnum.REFUND.getPayTypeCode());
        payOrderDO.setPayStatus(PayStatusEnum.INIT.getStatusCode());
        payOrderDO.setPayCreateDate(new Date());
        payOrderDO.setOrigPayNo(request.getOrigPayNo());
        payOrderDO.setOrderRemark(request.getRefundReason());

        return payOrderDO;

    }


    /**
     * 支付退款异步任务作业单.
     *
     * @param refundFundList 退款资金单.
     * @return 异步作业单.
     */
    private List<PayJobDetailDO> createRefundJobDetail(List<PayFundDO> refundFundList) {

        return refundFundList.stream().map(payFundDO -> {

            PayJobDetailDO payJobDetailDO = new PayJobDetailDO();

            payJobDetailDO.setJobDetailId(payFundDO.getPayFundNo());
            payJobDetailDO.setJobDetailDesc(JobTypeEnum.ASYNC_REFUND.getJobTypeDesc());
            payJobDetailDO.setJobType(JobTypeEnum.ASYNC_REFUND.getJobTypeCode());
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
     * 组装退款资金单.
     *
     * @param request    请求对象.
     * @param payOrderDO 支付单.
     */
    private void buildRefundFund(RefundRequestBO request, PayOrderDO payOrderDO) {

        // 查询所有支付资金单.
        List<PayFundDO> payFundDOList = payFundManager.queryPayFundListByPayNo(payOrderDO.getPayNo());

        // ---------------------------组装退款信息--------------------.
        // 外部渠道支付.
        List<PayFundDO> thirdPayFundList = Lists.newArrayList();
        // 余额支付类型.
        List<PayFundDO> balancePayFundList = Lists.newArrayList();
        // 营销支付列表.
        List<PayFundDO> marketingPayFundList = Lists.newArrayList();

        payFundDOList.forEach(payFundDO -> {
            if (PayToolEnum.isThirdPay(payFundDO.getPayTool())) {
                thirdPayFundList.add(payFundDO);
            } else if (PayToolEnum.isBalance(payFundDO.getPayTool())) {
                balancePayFundList.add(payFundDO);
            } else {
                marketingPayFundList.add(payFundDO);
            }
        });

        // 生成退款单号.
        request.setPayNo(paySeqNoCreator.createPaySeqNo(request.getRequestDate(), request.getBizLine(),
                PayTypeEnum.REFUND.getPayTypeCode(), RedisKeyEnum.PAYCORE_REFUND));
        request.setOrigPayNo(payOrderDO.getPayNo());

        // 退款逻辑计算(后期可以根据业务传入可配置退款).
        calRefundAmt(thirdPayFundList, balancePayFundList, marketingPayFundList, request);
    }

    /**
     * 计算退款.
     *
     * @param thirdPayFundList     外部渠道支付资金单.
     * @param balancePayFundList   余额支付资金单.
     * @param marketingPayFundList 营销支付资金单.
     * @param request              退款请求对象.
     */
    private void calRefundAmt(List<PayFundDO> thirdPayFundList, List<PayFundDO> balancePayFundList,
                              List<PayFundDO> marketingPayFundList, RefundRequestBO request) {


        // 退款金额.
        long refundSurplusAmt = request.getPayAmt();


        long refundThirdPayAmt = refundableAmt(thirdPayFundList);

        // 外部渠道资金足够.
        if (refundThirdPayAmt >= refundSurplusAmt) {

            // 1、外部渠道资金扣除.
            refundFundProcess(request, thirdPayFundList, refundSurplusAmt);

        } else {

            // 1、外部渠道资金扣除.
            refundFundProcess(request, thirdPayFundList, refundThirdPayAmt);

            // 剩余退款金额.
            refundSurplusAmt = refundSurplusAmt - refundThirdPayAmt;

            // 计算余额足够.
            long refundBalanceAmt = refundableAmt(balancePayFundList);

            // 余额退款.
            if (refundBalanceAmt >= refundSurplusAmt) {

                // 2、余额资金扣除.
                refundFundProcess(request, balancePayFundList, refundSurplusAmt);

            } else {

                // 剩余营销资金.
                refundSurplusAmt = refundSurplusAmt - refundBalanceAmt;

                // 3、营销资金扣除.
                refundFundProcess(request, marketingPayFundList, refundSurplusAmt);
            }

        }
    }


    /**
     * 退款资金单处理(当前资金扣除金额,所有资金单金额是可以满足的,需要按照可退款金额排序按照顺序退款).
     *
     * @param request     请求对象.
     * @param payFundList 支付资金单列表.
     * @param refundAmt   退款金额.
     */
    private void refundFundProcess(RefundRequestBO request, List<PayFundDO> payFundList, long refundAmt) {

        // 可退款金额为空.
        if (refundAmt == 0L) {
            return;
        }

        long refundSurplusAmt = refundAmt;

        // todo 暂时只按照顺序扣除.
        for (PayFundDO payFundDO : payFundList) {

            // 当前扣除资金.
            long currentRefundAmt = payFundDO.getPayAmt() - payFundDO.getRefundAmt();

            if (currentRefundAmt == 0L) {
                continue;
            }

            // 资金满足要求,跳出循环.
            if (currentRefundAmt >= refundSurplusAmt) {
                addReadyRefundFundList(request, payFundDO, refundSurplusAmt);
                break;
            }

            // 继续循环.
            refundSurplusAmt -= currentRefundAmt;
        }
    }

    /**
     * 添加到待退款列表中.
     *
     * @param request   退款请求对象.
     * @param payFundDO 支付资金单.
     * @param refundAmt 退款金额.
     */
    private void addReadyRefundFundList(RefundRequestBO request, PayFundDO payFundDO, long refundAmt) {

        // 原单已退款金额.
        payFundDO.setRefundAmt(payFundDO.getRefundAmt() + refundAmt);
        request.getRefundOrigPayFundBOList().add(BeanUtil.copyProperties(payFundDO, PayFundBO.class));

        // 新增退款单.
        PayFundBO refundFundBO = new PayFundBO();
        request.getRefundFundBOList().add(refundFundBO);

        refundFundBO.setPayNo(request.getPayNo());
        refundFundBO.setPayType(PayToolEnum.isBalance(payFundDO.getPayTool()) ? PayTypeEnum.TRANSFER.getPayTypeCode() :
                PayTypeEnum.REFUND.getPayTypeCode());
        refundFundBO.setPayFundNo(paySeqNoCreator.createPaySeqNo(request.getRequestDate(), request.getBizLine(),
                refundFundBO.getPayType(), RedisKeyEnum.PAYCORE_PAY_FUND_NO));
        refundFundBO.setBizType(BizTypeEnum.DEFAULT.getBizTypeCode());
        refundFundBO.setPayCreateDate(new Date());
        refundFundBO.setPayAmt(refundAmt);
        refundFundBO.setOrigPayFundNo(payFundDO.getPayFundNo());
        refundFundBO.setRefundAmt(0L);
        refundFundBO.setPayTool(payFundDO.getPayTool());
        refundFundBO.setPaySubTool(payFundDO.getPaySubTool());
        refundFundBO.setPayStatus(PayStatusEnum.INIT.getStatusCode());
        refundFundBO.setPaySeq(request.getRefundFundBOList().size());
        refundFundBO.setRevokeFlag(FlagsEnum.FALSE.getFlagCode());
    }

    /**
     * 可退金额.
     *
     * @param payFundList 支付资金单.
     * @return 可退金额.
     */
    private long refundableAmt(List<PayFundDO> payFundList) {

        // 资金单为空.
        if (CollectionUtil.isEmpty(payFundList)) {
            return 0L;
        }

        return payFundList.stream().mapToLong(payFundDO -> payFundDO.getPayAmt() - payFundDO.getRefundAmt()).sum();
    }

    /**
     * 退款验证.
     *
     * @param request    退款请求对象.
     * @param payOrderDO 支付单.
     */
    private void refundVerify(RefundRequestBO request, PayOrderDO payOrderDO) {

        // 支付单必须为成功.
        if (!StrUtil.equals(PayStatusEnum.SUCCESS.getStatusCode(), payOrderDO.getPayStatus())) {
            PayException.throwBizFailException(ResponseCodeEnum.PAY_STATUS_NOT_SUPPORT);
        }

        // 已退款检查.
        if (request.getPayAmt() + payOrderDO.getRefundAmt() > payOrderDO.getPayAmt()) {
            PayException.throwBizFailException(ResponseCodeEnum.REFUND_AMT_OVERMUCH);
        }
    }
}
