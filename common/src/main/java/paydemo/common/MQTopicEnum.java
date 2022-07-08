package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * MQTopic.
 */
@Getter
@AllArgsConstructor
public enum MQTopicEnum {

    /**
     * 支付结果通知.
     */
    PAY_RESULT_TOPIC("pay_result_topic","支付结果通知"),

    /**
     * 内部补单.
     */
    PAY_INNER_PUSH_STATUS("pay_inner_push_status_topic","内部补单"),

    ;

    private final String topicCode;

    private final String topicDesc;
}
