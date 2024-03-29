package paydemo.manager.mq.rocketmq;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.google.common.eventbus.AsyncEventBus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import paydemo.common.ExecStatusEnum;
import paydemo.common.JobTypeEnum;
import paydemo.common.MQDelayLevelEnum;
import paydemo.dao.dbmodel.PayJobDetailDO;
import paydemo.manager.db.PayJobDetailManager;
import paydemo.manager.mq.local.event.BaseLocalEvent;

import java.util.Date;
import java.util.UUID;

/**
 * @auther YDXiaa
 * <p>
 * message producer.
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class MessageProducer {

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private PayJobDetailManager payJobDetailManager;

    /**
     * 本地事件驱动模型.
     */
    public <T extends BaseLocalEvent> void sendLocalMessage(T message) {
        asyncEventBus.post(message);
    }

    /**
     * 发送实时消息.
     */
    public void sendRealTimeMessage(String message, String topic) {
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送消息成功,发送结果:{}", sendResult);
                // 只有SendOk才视为成功.
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    addPayJobDetailForRetry(message);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("发送消息失败,异常信息:{}", throwable);
                addPayJobDetailForRetry(message);
            }
        });
    }

    /**
     * 发送延迟消息.
     *
     * @param message    发送消息.
     * @param topic      topic.
     * @param delayLevel 延迟级别.
     */
    public void sendDelayMessage(String message, String topic, MQDelayLevelEnum delayLevel) {

        // 实时消息.
        if (MQDelayLevelEnum.LEVEL_0S == delayLevel) {
            sendRealTimeMessage(message, topic);
            return;
        }


        Message<String> stringMessage = MessageBuilder.withPayload(message).build();

        // 只有同步发送才能指定延迟级别.
        try {

            log.info("发送延迟消息:{},topic:{}",message,topic);
            SendResult sendResult = rocketMQTemplate.syncSend(topic, stringMessage, 3000L, delayLevel.getDelayLevelSeq());

            // 只有SendOk才视为成功.
            if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                addPayJobDetailForRetry(message);
            }
        } catch (Throwable throwable) {
            log.error("发送延迟消息失败,消息体:{},异常信息:{}", message, ExceptionUtil.stacktraceToString(throwable));
            addPayJobDetailForRetry(message);
        }
    }

    /**
     * 插入作业单.
     *
     * @param message message.
     */
    private void addPayJobDetailForRetry(String message) {

//        PayJobDetailDO payJobDetailDO = PayJobDetailDO.builder().jobDetailId(UUID.randomUUID().toString())
//                .jobDetailDesc(message)
//                .jobType(JobTypeEnum.MQ_RETRY_SEND.getJobTypeCode())
//                .execTimes(0L)
//                .failRetryTimes(5L)
//                .execInterval(5L)
//                .shardingMark(1L)
//                .execStatus(ExecStatusEnum.READY.getExecStatusCode())
//                .nextExecTime(new Date())
//                .build();
//
//        payJobDetailManager.addJobDetail(payJobDetailDO);
    }
}
