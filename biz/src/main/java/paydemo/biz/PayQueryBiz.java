package paydemo.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.MQDelayLevelEnum;
import paydemo.common.MQTopicEnum;
import paydemo.common.RemotePayResult;
import paydemo.common.VerifyUtil;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.manager.db.PayFundManager;
import paydemo.manager.db.PayOrderManager;
import paydemo.manager.model.InnerPushMQMessage;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.model.PayRequestBO;
import paydemo.manager.model.PayResponseBO;
import paydemo.manager.mq.rocketmq.MessageProducer;
import paydemo.util.JsonUtil;
import paydemo.util.PayStatusEnum;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @auther YDXiaa
 * <p>
 * 支付查询biz.
 * 1、查询订单.
 * 2、补单推进查询.
 */
@Service
public class PayQueryBiz {

    @Autowired
    private PayOrderManager payOrderManager;

    @Autowired
    private PayFundManager payFundManager;

    @Autowired
    private PayProcessorFactory processorFactory;

    @Autowired
    private PayBiz payBiz;

    @Autowired
    private MessageProducer messageProducer;


    /**
     * 1、查询订单.
     *
     * @param request 请求对象.
     * @return 支付响应.
     */
    public PayResponseBO queryPayOrder(PayRequestBO request) {

        PayOrderDO payOrderDO = payOrderManager.queryPayOrder(request.getRequestBizNo(), request.getRequestDate(), request.getBizLine());

        if (Objects.isNull(payOrderDO)) {
            PayException.throwBizFailException(ResponseCodeEnum.ORDER_SYS_EXISTS);
        }

        return BeanUtil.copyProperties(payOrderDO, PayResponseBO.class);
    }

    /**
     * 2、订单推进查询.
     *
     * @param pushMQMessage 内部推进消息.
     */
    public void pushStatusQuery(InnerPushMQMessage pushMQMessage) {

        // 查询支付单.
        PayOrderDO payOrderDO = payOrderManager.queryPayOrderByPayNo(pushMQMessage.getPayNo());

        if (Objects.isNull(payOrderDO)) {
            return;
        }

        // 已经是最终态不处理.
        if (StrUtil.equalsAny(payOrderDO.getPayStatus(), PayStatusEnum.SUCCESS.getStatusCode(), PayStatusEnum.FAIL.getStatusCode())) {
            return;
        }

        // 查询所有资金单.
        List<PayFundBO> payFundBOList = BeanUtil.copyToList(payFundManager.queryPayFundListByPayNo(pushMQMessage.getPayNo()), PayFundBO.class);

        // 得到目标资金单.
        PayFundBO payFundBO = payFundBOList.stream().filter(payFund -> StrUtil.equals(payFund.getPayFundNo(), pushMQMessage.getPayFundNo()))
                .findFirst().orElseThrow(() -> new PayException(ResponseCodeEnum.SYS_ERROR));

        // 订单已经是终态不处理.
        if (StrUtil.equalsAny(payFundBO.getPayStatus(), PayStatusEnum.SUCCESS.getStatusCode(), PayStatusEnum.FAIL.getStatusCode())) {
            return;
        }

        // 支付结果处理.
        RemotePayResult remotePayResult = processorFactory.getPayProcessor(payFundBO.getPayTool()).payQuery(payFundBO);

        // 处理订单.
        modifyPayOrderStatus(remotePayResult, payFundBO);

        // continueCompleteOrder.
        PayRequestBO payRequestBO = createPayRequestBO(payOrderDO, payFundBOList, remotePayResult, payFundBO);

        // 查询结果判断.
        if (StrUtil.equals(PayStatusEnum.PAYING.getStatusCode(), remotePayResult.getPayStatus()) && InnerPushMQMessage.reSetPushStatuTimes(pushMQMessage)) {
            doNextPushStatusQuery(pushMQMessage);
        } else {
            doContinueCompletePayOrder(payRequestBO);
        }
    }

    /**
     * 创建支付请求对象.
     *
     * @param payOrderDO      支付单.
     * @param payFundBOList   支付资金单.
     * @param remotePayResult 最后一笔支付结果.
     * @param payFundBO       最后一笔支付资金单.
     * @return 支付请求对象.
     */
    private PayRequestBO createPayRequestBO(PayOrderDO payOrderDO, List<PayFundBO> payFundBOList, RemotePayResult remotePayResult,
                                            PayFundBO payFundBO) {

        PayRequestBO payRequestBO = BeanUtil.copyProperties(payOrderDO, PayRequestBO.class);
        Collections.sort(payFundBOList);

        payRequestBO.setPayFundBOList(payFundBOList);
        payRequestBO.setLastRemotePayResult(remotePayResult);

        // 设置原单通道号.
        remotePayResult.setChannelDetailNo(payFundBO.getChannelDetailNo());

        return payRequestBO;
    }

    /**
     * 更新订单信息.
     *
     * @param remotePayResult 支付结果.
     * @param payFundBO       支付资金单.
     */
    private void modifyPayOrderStatus(RemotePayResult remotePayResult, PayFundBO payFundBO) {

        payFundBO.setPayStatus(remotePayResult.getPayStatus());
        payFundBO.setResultCode(remotePayResult.getResultCode());
        payFundBO.setResultDesc(remotePayResult.getResultDesc());

        int modifyCnt = payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.PAYING);

        VerifyUtil.verifySqlResult(modifyCnt);
    }

    /**
     * 再次发送MQ消息补单.
     *
     * @param pushMQMessage MQ消息.
     */
    private void doNextPushStatusQuery(InnerPushMQMessage pushMQMessage) {

        // 使用rocketMQ延迟消息补单.
        messageProducer.sendDelayMessage(JsonUtil.toJsonStr(pushMQMessage), MQTopicEnum.PAY_INNER_PUSH_STATUS.getTopicCode(),
                MQDelayLevelEnum.PAY_PUSH_INTERVAL[pushMQMessage.getPushPayStatusTimes()]);

    }

    /**
     * 继续完成订单.
     *
     * @param payRequestBO 支付单.
     */
    private void doContinueCompletePayOrder(PayRequestBO payRequestBO) {
        payRequestBO.setContinueCompletePayOrder(true);
        payBiz.execute(payRequestBO);
    }
}
