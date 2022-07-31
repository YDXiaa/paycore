package paydemo.biz.mqconsumer;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.biz.PayQueryBiz;
import paydemo.common.PushQueryTypeEnum;
import paydemo.manager.model.InnerPushMQMessage;
import paydemo.util.JsonUtil;

/**
 * @auther YDXiaa
 * <p>
 * RocketMQ订单推进状态消费者.
 */
@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "paycore_consumer", topic = "pay_inner_push_status_topic")
public class PushStatusMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private PayQueryBiz payQueryBiz;

    @Override
    public void onMessage(String message) {
        try {
            log.info("收到内部补单消息:{}", message);

            InnerPushMQMessage pushMQMessage = JsonUtil.parseJsonStr2Obj(message, InnerPushMQMessage.class);

            // 处理MQ消息
            processPushMQMessage(pushMQMessage);

        } catch (Throwable throwable) {
            log.error("接收mq消息处理异常:{}", ExceptionUtil.stacktraceToString(throwable));
        }
    }

    /**
     * 处理MQ消息.
     *
     * @param pushMQMessage mq消息.
     */
    private void processPushMQMessage(InnerPushMQMessage pushMQMessage) {

        switch (PushQueryTypeEnum.match(pushMQMessage.getPushType())){

            case PAY_QUERY:
                payQueryBiz.pushStatusQuery(pushMQMessage);
                break;

            case REFUND_QUERY:
                break;

            default:
                break;
        }
    }
}
