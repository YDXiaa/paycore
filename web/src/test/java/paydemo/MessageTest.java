package paydemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.MQDelayLevelEnum;
import paydemo.common.MQTopicEnum;
import paydemo.manager.mq.local.event.PayResultEvent;
import paydemo.manager.mq.rocketmq.MessageProducer;

import java.util.concurrent.locks.LockSupport;

/**
 * @auther YDXiaa
 * <p>
 * 消息发送/MQ测试类.
 */
public class MessageTest extends BaseSpringBootSupport {

    @Autowired
    private MessageProducer messageProducer;


    @Test
    public void sendLocalMessage() {

        PayResultEvent payResultEvent = new PayResultEvent();
        payResultEvent.setPayStatus("SUCCESS");
        payResultEvent.setChannelDetailNo("test");

        messageProducer.sendLocalMessage(payResultEvent);
    }

    @Test
    public void sendMq() {
        messageProducer.sendDelayMessage("PUSH_STATUS", MQTopicEnum.PAY_INNER_PUSH_STATUS.getTopicCode(),
                MQDelayLevelEnum.LEVEL_30S);

        LockSupport.park();
    }

}
