package paydemo.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.manager.mq.rocketmq.MessageProducer;

/**
 * @auther YDXiaa
 * <p>
 * RocketMQ消息重试发送.
 */
@Service
public class MqRetrySendBiz {

    @Autowired
    private MessageProducer messageProducer;

    /**
     * 发送RocketMQ消息.
     *
     * @param payFundNo 支付资金单号.
     * @return 发送消息结果.
     */
    public boolean sendMQMessage(String payFundNo) {


        return true;
    }
}
