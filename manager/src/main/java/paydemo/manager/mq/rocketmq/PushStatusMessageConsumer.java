package paydemo.manager.mq.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @auther YDXiaa
 * <p>
 * RocketMQ订单推进状态消费者.
 */
@Service
@RocketMQMessageListener(consumerGroup = "paycore_consumer", topic = "pay_inner_push_status_topic")
public class PushStatusMessageConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        try {

            System.out.println("内部补单任务...." + message);

        } catch (Throwable throwable) {

        }
    }
}
