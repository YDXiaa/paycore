package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 补单查询类型.
 */
@Getter
@AllArgsConstructor
public enum PushQueryTypeEnum {

    /**
     * 支付查询.
     */
    PAY_QUERY(2, "PAY_QUERY", "支付查询"),

    /**
     * 退款查询.
     */
    REFUND_QUERY(10, "REFUND_QUERY", "退款查询"),

    /**
     * 冲正查询.
     */
    REVOKE_QUERY(10, "REVOKE_QUERY", "冲正查询"),

    ;

    /**
     * 查询次数.
     */
    private final int queryTimes;

    private final String pushQueryTypeCode;

    private final String pushQueryTypeDesc;

    /**
     * 匹配补单查询类型.
     *
     * @param pushType 补单查询类型.
     * @return 补单查询类型.
     */
    public static PushQueryTypeEnum match(String pushType) {

        for (PushQueryTypeEnum pushQueryTypeEnum : PushQueryTypeEnum.values()) {
            if (pushQueryTypeEnum.getPushQueryTypeCode().equals(pushType)) {
                return pushQueryTypeEnum;
            }
        }

        return PushQueryTypeEnum.PAY_QUERY;
    }
}
