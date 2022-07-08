package paydemo.manager.remote.thirdpay;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.*;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.manager.mq.rocketmq.MessageProducer;

/**
 * @auther YDXiaa
 * <p>
 * 服务执行器.
 */
@Slf4j
@Service
public class ServiceExecutor {

    @Autowired
    private ThirdPayServiceFactory payServiceFactory;

    @Autowired
    private MessageProducer messageProducer;

    /**
     * 远程调用.
     *
     * @param requestModel 远程调用参数.
     * @return 执行结果.
     */
    public RemotePayResult executeRemoteInvoke(RemoteRequestModel requestModel) {

        RemotePayResult remotePayResult = new RemotePayResult();
        try {
            String serviceKey = KeyCreator.createKey(requestModel.getClassPackageName(), requestModel.getMethodName());
            remotePayResult = payServiceFactory.getServiceAdapter(serviceKey).service(requestModel);
        } catch (RpcException rpcException) {
            if (rpcException.isTimeout()) {
                remotePayResult.setPayStatus(PayStatusEnum.PAYING.getStatusCode());
                remotePayResult.setResultCode(ResponseCodeEnum.REQUEST_CHANNEL_TIMEOUT.getRespCode());
                remotePayResult.setResultDesc(ResponseCodeEnum.REQUEST_CHANNEL_TIMEOUT.getRespDesc());
                // 补单查询任务.
                pushOrderStatusQuery(requestModel);
            } else {
                remotePayResult.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
                remotePayResult.setResultCode(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespCode());
                remotePayResult.setResultDesc(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespDesc());
            }
        } catch (Throwable throwable) {
            remotePayResult.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
            remotePayResult.setResultCode(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespCode());
            remotePayResult.setResultDesc(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespDesc());
        }

        // 渠道请求流水号.
        remotePayResult.setOutRequestSeqNo(requestModel.getOutRespSeqNo());

        return remotePayResult;
    }

    /**
     * 渠道侧掉单,开启补单查询任务.
     *
     * @param requestModel 远程请求模型.
     */
    private void pushOrderStatusQuery(RemoteRequestModel requestModel) {

        // 使用rocketMQ延迟消息补单.
        messageProducer.sendDelayMessage(JsonUtil.toJsonStr(requestModel), MQTopicEnum.PAY_INNER_PUSH_STATUS.getTopicCode(),
                MQDelayLevelEnum.LEVEL_10S);
    }
}
