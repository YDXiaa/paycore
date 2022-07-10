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
     * 5分钟.
     */
    LEVEL_5M(9, "5m"),

    ;


    /**
     * 延迟级别序号.
     */
    private final int delayLevelSeq;

    /**
     * 延迟级别描述.
     */
    private final String delayLevelDesc;

}
