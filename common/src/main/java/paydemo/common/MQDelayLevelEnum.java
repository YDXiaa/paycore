package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * rocketMQ消息延迟级别.
 * <p>
 * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h.
 */
@Getter
@AllArgsConstructor
public enum MQDelayLevelEnum {

    /**
     * 实时消息.
     */
    LEVEL_0S(0, "0s"),

    /**
     * 1s.
     */
    LEVEL_1S(1, "1s"),

    /**
     * 5s.
     */
    LEVEL_5S(2, "5s"),

    /**
     * 10s.
     */
    LEVEL_10S(3, "10s"),

    /**
     * 30s.
     */
    LEVEL_30S(4, "30s"),

    /**
     * 1分钟.
     */
    LEVEL_1M(5, "1m"),

    /**
     * 2分钟.
     */
    LEVEL_2M(6, "2m"),

    /**
     * 3分钟.
     */
    LEVEL_3M(7, "1m"),

    /**
     * 4分钟.
     */
    LEVEL_4M(8, "1m"),

    /**
     * 5分钟.
     */
    LEVEL_5M(9, "5m"),

    /**
     * 6分钟.
     */
    LEVEL_6M(10, "6m"),

    /**
     * 7分钟.
     */
    LEVEL_7M(11, "7m"),

    /**
     * 8分钟.
     */
    LEVEL_8M(12, "8m"),

    /**
     * 9分钟.
     */
    LEVEL_9M(13, "9m"),

    /**
     * 10分钟.
     */
    LEVEL_10M(14, "10m"),

    /**
     * 20分钟.
     */
    LEVEL_20M(15, "20m"),

    /**
     * 30分钟.
     */
    LEVEL_30M(16, "30m"),

    /**
     * 1小时.
     */
    LEVEL_1H(17, "1h"),

    /**
     * 2小时.
     */
    LEVEL_2H(18, "2h"),
    ;

    /**
     * 延迟级别序号.
     */
    private final int delayLevelSeq;

    /**
     * 延迟级别描述.
     */
    private final String delayLevelDesc;


    // 支付补单间隔.
    public static final MQDelayLevelEnum[] PAY_PUSH_INTERVAL = new MQDelayLevelEnum[]{
            LEVEL_0S, LEVEL_5S,
            LEVEL_5S, LEVEL_5S,
            LEVEL_5S, LEVEL_10S,
            LEVEL_30S, LEVEL_1M,
            LEVEL_5M, LEVEL_30M
    };


}
