package paydemo.manager.model;

import lombok.Builder;
import lombok.Data;

/**
 * @auther YDXiaa
 * <p>
 * 内部推进mq消息.
 */
@Data
@Builder
public class InnerPushMQMessage {

    /**
     * 支付单号.
     */
    private String payNo;

    /**
     * 支付资金单.
     */
    private String payFundNo;

    /**
     * 支付类型.
     */
    private String payType;

    /**
     * 支付推进类型.
     */
    private String pushType;

    /**
     * 状态同步次数.
     */
    private int pushPayStatusTimes;

    /**
     * 最大同步次数.
     */
    private int maxPushPayStatusTimes;

    /**
     * 重新设置推进次数.
     *
     * @param innerPushMQMessage mqMessage.
     */
    public static boolean reSetPushStatuTimes(InnerPushMQMessage innerPushMQMessage) {

        // 超出最大执行次数.
        if (innerPushMQMessage.getPushPayStatusTimes() >= innerPushMQMessage.getMaxPushPayStatusTimes()) {
            return false;
        }

        // 重新设置执行次数.
        innerPushMQMessage.setPushPayStatusTimes(innerPushMQMessage.getPushPayStatusTimes() + 1);

        return true;
    }

}
