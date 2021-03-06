package paydemo.biz.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.biz.route.RouteBiz;
import paydemo.common.*;
import paydemo.manager.db.PayFundManager;
import paydemo.manager.helper.BeanCopier;
import paydemo.manager.helper.PaySeqNoCreator;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.manager.mq.rocketmq.MessageProducer;
import paydemo.manager.remote.thirdpay.ServiceExecutor;

/**
 * @auther YDXiaa
 * <p>
 * 三方支付处理器.
 */
@Service
public class ThirdPayProcessor implements PayProcessor {

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
        payFundBO.setPayStatus(PayStatusEnum.PAYING.getStatusCode());
        // 更新支付状态为PAYING.
        int moidfyResult = payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.INIT);
        VerifyUtil.verifySqlResult(moidfyResult);

        // 路由流程.
        PayBaseInfoBO payBaseInfoBO = BeanCopier.objCopy(payFundBO, PayBaseInfoBO.class);
        ChannelDetailInfoBO channelDetailInfoBO = routeBiz.route(payBaseInfoBO);

        payFundBO.setChannelDetailNo(channelDetailInfoBO.getChannelDetailNo());
        payFundBO.setChannelFeeAmt(channelDetailInfoBO.getChannelFeeAmt());
        payFundBO.setClassPackageName(channelDetailInfoBO.getClassPackageName());
        payFundBO.setMethodName(channelDetailInfoBO.getMethodName());
        // 生成请求流水号.
        payFundBO.setOutRequestSeqNo(paySeqNoCreator.createChannelSeqNo(payFundBO.getPayCreateDate(),
                channelDetailInfoBO.getChannelSeqLen()));

        RemoteRequestModel requestModel = BeanCopier.objCopy(payFundBO, RemoteRequestModel.class);
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
            pushOrderStatusQuery(requestModel);
        }

        // 更新支付结果.
        payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.PAYING);

        return remotePayResult;
    }

    @Override
    public RemotePayResult revoke(PayFundBO payFundBO) {
        return null;
    }


    /**
     * 渠道侧掉单,开启补单查询任务.
     *
     * @param requestModel 远程请求模型.
     */
    private void pushOrderStatusQuery(RemoteRequestModel requestModel) {

        // 非查询任务才需要补单.

        // 使用rocketMQ延迟消息补单.
        messageProducer.sendDelayMessage(JsonUtil.toJsonStr(requestModel), MQTopicEnum.PAY_INNER_PUSH_STATUS.getTopicCode(),
                MQDelayLevelEnum.LEVEL_10S);
    }
}
