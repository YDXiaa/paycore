package paydemo.biz.processor;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.biz.route.RouteBiz;
import paydemo.common.*;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.db.PayFundManager;
import paydemo.manager.helper.PaySeqNoCreator;
import paydemo.manager.model.*;
import paydemo.manager.mq.rocketmq.MessageProducer;
import paydemo.manager.remote.thirdpay.ServiceExecutor;
import paydemo.util.JsonUtil;
import paydemo.util.PayStatusEnum;

import java.util.Objects;

/**
 * @auther YDXiaa
 * <p>
 * 三方支付处理器.
 */
@Service
public class ThirdPayProcessor extends BasePayProcess implements PayProcessor {

    @Autowired
    private RouteBiz routeBiz;

    @Autowired
    private PaySeqNoCreator paySeqNoCreator;

    @Autowired
    private ServiceExecutor serviceExecutor;

    @Autowired
    private PayFundManager payFundManager;

    @Autowired
    private MessageProducer messageProducer;


    @Override
    public RemotePayResult pay(PayFundBO payFundBO) {

        // 路由流程.
        PayBaseInfoBO payBaseInfoBO = BeanUtil.copyProperties(payFundBO, PayBaseInfoBO.class);
        ChannelDetailInfoBO channelDetailInfoBO = routeBiz.route(payBaseInfoBO);

        // 渠道路由为空，直接失败处理.
        if (Objects.isNull(channelDetailInfoBO)) {
            return payFailResultProcess(payFundBO);
        }

        payFundBO.setChannelDetailNo(channelDetailInfoBO.getChannelDetailNo());
        payFundBO.setChannelFeeAmt(channelDetailInfoBO.getChannelFeeAmt());
        payFundBO.setClassPackageName(channelDetailInfoBO.getClassPackageName());
        payFundBO.setMethodName(channelDetailInfoBO.getMethodName());
        // 生成请求流水号.
        payFundBO.setOutRequestSeqNo(paySeqNoCreator.createChannelSeqNo(payFundBO.getPayCreateDate(),
                channelDetailInfoBO.getChannelSeqLen()));

        // 支付前处理资金单支付状态.
        payBeforePayStatusProcess(payFundBO);

        RemoteRequestModel requestModel = BeanUtil.copyProperties(payFundBO, RemoteRequestModel.class);
        RemotePayResult remotePayResult = serviceExecutor.executeRemoteInvoke(requestModel);

        // 设置支付单状态.
        payFundBO.setPayStatus(remotePayResult.getPayStatus());
        payFundBO.setOutRespSeqNo(remotePayResult.getOutRespSeqNo());
        payFundBO.setResultCode(remotePayResult.getResultCode());
        payFundBO.setResultDesc(remotePayResult.getResultDesc());

        // 设置支付渠道详情编码,后续成功率统计要使用.
        remotePayResult.setChannelDetailNo(channelDetailInfoBO.getChannelDetailNo());

        // 内部补单.
        if (PayStatusEnum.PAYING.getStatusCode().equals(remotePayResult.getPayStatus())) {
            requestModel.setPushQueryTypeEnum(PushQueryTypeEnum.PAY_QUERY);
            pushOrderStatusQuery(requestModel);
        }

        // 支付结果处理.
        payResultStatusProcess(payFundBO);

        return remotePayResult;
    }

    /**
     * 支付失败处理.
     *
     * @param payFundBO 支付资金单.
     * @return 远程支付结果.
     */
    private RemotePayResult payFailResultProcess(PayFundBO payFundBO) {

        payFundBO.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
        // 更新支付状态为PAYING.
        int moidfyResult = payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.INIT);
        VerifyUtil.verifySqlResult(moidfyResult);

        RemotePayResult remotePayResult = new RemotePayResult();

        remotePayResult.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
        remotePayResult.setResultCode(ResponseCodeEnum.USABLE_CHANNEL_NOT_EXISTS.getRespCode());
        remotePayResult.setResultDesc(ResponseCodeEnum.USABLE_CHANNEL_NOT_EXISTS.getRespDesc());

        return remotePayResult;
    }

    @Override
    public RemotePayResult payQuery(PayFundBO payFundBO) {

        PayBaseInfoBO payBaseInfoBO = PayBaseInfoBO.builder().payTool(payFundBO.getPayTool())
                .paySubTool(payFundBO.getPaySubTool())
                .payChannelDetailNo(payFundBO.getChannelDetailNo())
                .payType(payFundBO.getPayType())
                .bizType(BizTypeEnum.ORDER_QUERY.getBizTypeCode())
                .build();

        // 路由逻辑.
        ChannelDetailInfoBO channelDetailInfoBO = routeBiz.route(payBaseInfoBO);

        RemoteRequestModel requestModel = RemoteRequestModel.builder().outRequestSeqNo(payFundBO.getOutRequestSeqNo())
                .outRespSeqNo(payFundBO.getOutRespSeqNo())
                .outRequestDate(payFundBO.getPayCreateDate())
                .channelNo(channelDetailInfoBO.getChannelNo())
                .channelDetailNo(channelDetailInfoBO.getChannelDetailNo())
                .classPackageName(channelDetailInfoBO.getClassPackageName())
                .methodName(channelDetailInfoBO.getMethodName())
                .build();

        // 远程调用.
        return serviceExecutor.executeRemoteInvoke(requestModel);
    }

    @Override
    public RemotePayResult revoke(PayFundBO payFundBO) {

        // 路由流程.
        PayBaseInfoBO payBaseInfoBO = PayBaseInfoBO.builder().payTool(payFundBO.getPayTool())
                .paySubTool(payFundBO.getPaySubTool())
                .payType(payFundBO.getPayType())
                .availablePayTypes(Lists.newArrayList(PayTypeEnum.CANCEL.getPayTypeCode(),
                        PayTypeEnum.REFUND.getPayTypeCode(), PayTypeEnum.REVOKE.getPayTypeCode()))
                .payChannelDetailNo(payFundBO.getChannelDetailNo())
                .build();


        ChannelDetailInfoBO channelDetailInfoBO = routeBiz.route(payBaseInfoBO);

        // 渠道路由为空，直接失败处理.
        if (Objects.isNull(channelDetailInfoBO)) {
            return payFailResultProcess(payFundBO);
        }

        payFundBO.setChannelDetailNo(channelDetailInfoBO.getChannelDetailNo());
        payFundBO.setChannelFeeAmt(channelDetailInfoBO.getChannelFeeAmt());
        payFundBO.setClassPackageName(channelDetailInfoBO.getClassPackageName());
        payFundBO.setMethodName(channelDetailInfoBO.getMethodName());
        // 生成请求流水号.
        payFundBO.setOutRequestSeqNo(paySeqNoCreator.createChannelSeqNo(payFundBO.getPayCreateDate(),
                channelDetailInfoBO.getChannelSeqLen()));

        // 支付前处理支付资金单状态.
        payBeforePayStatusProcess(payFundBO);

        RemoteRequestModel requestModel = BeanUtil.copyProperties(payFundBO, RemoteRequestModel.class);
        RemotePayResult remotePayResult = serviceExecutor.executeRemoteInvoke(requestModel);

        // 设置支付单状态.
        payFundBO.setPayStatus(remotePayResult.getPayStatus());
        payFundBO.setOutRespSeqNo(remotePayResult.getOutRespSeqNo());
        payFundBO.setResultCode(remotePayResult.getResultCode());
        payFundBO.setResultDesc(remotePayResult.getResultDesc());

        // 设置支付渠道详情编码,后续成功率统计要使用.
        remotePayResult.setChannelDetailNo(channelDetailInfoBO.getChannelDetailNo());

        // 内部补单.
        if (PayStatusEnum.PAYING.getStatusCode().equals(remotePayResult.getPayStatus())) {
            requestModel.setPushQueryTypeEnum(PushQueryTypeEnum.REVOKE_QUERY);
            pushOrderStatusQuery(requestModel);
        }

        // 更新支付结果.
        payResultStatusProcess(payFundBO);

        return remotePayResult;
    }

    @Override
    public RemotePayResult revokeQuery(PayFundBO payFundBO) {

        PayBaseInfoBO payBaseInfoBO = PayBaseInfoBO.builder().payTool(payFundBO.getPayTool())
                .paySubTool(payFundBO.getPaySubTool())
                .payChannelDetailNo(payFundBO.getChannelDetailNo())
                .payType(payFundBO.getPayType())
                .build();

        // 路由逻辑.
        ChannelDetailInfoBO channelDetailInfoBO = routeBiz.route(payBaseInfoBO);

        RemoteRequestModel requestModel = RemoteRequestModel.builder().outRequestSeqNo(payFundBO.getOutRequestSeqNo())
                .outRespSeqNo(payFundBO.getOutRespSeqNo())
                .outRequestDate(payFundBO.getPayCreateDate())
                .channelNo(channelDetailInfoBO.getChannelNo())
                .channelDetailNo(channelDetailInfoBO.getChannelDetailNo())
                .classPackageName(channelDetailInfoBO.getClassPackageName())
                .methodName(channelDetailInfoBO.getMethodName())
                .build();

        // 远程调用.
        return serviceExecutor.executeRemoteInvoke(requestModel);
    }


    /**
     * 渠道侧掉单,开启补单查询任务.
     *
     * @param requestModel 远程请求模型.
     */
    private void pushOrderStatusQuery(RemoteRequestModel requestModel) {

        // 非查询任务才需要补单.
        InnerPushMQMessage innerPushMQMessage = InnerPushMQMessage.builder().payNo(requestModel.getPayNo())
                .payFundNo(requestModel.getPayFundNo())
                .payType(requestModel.getPayType())
                .pushType(requestModel.getPushQueryTypeEnum().getPushQueryTypeCode())
                .pushPayStatusTimes(1)
                .maxPushPayStatusTimes(requestModel.getPushQueryTypeEnum().getQueryTimes())
                .build();

        // 使用rocketMQ延迟消息补单.
        messageProducer.sendDelayMessage(JsonUtil.toJsonStr(innerPushMQMessage), MQTopicEnum.PAY_INNER_PUSH_STATUS.getTopicCode(),
                MQDelayLevelEnum.PAY_PUSH_INTERVAL[innerPushMQMessage.getPushPayStatusTimes()]);
    }
}
