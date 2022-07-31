package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 任务类型.
 */
@Getter
@AllArgsConstructor
public enum JobTypeEnum {

    /**
     * 支付冲正.
     */
    PAY_REVOKE("PAY_REVOKE", "支付冲正"),

    /**
     * MQ消息重试.
     */
    MQ_RETRY_SEND("MQ_RETRY_SEND", "MQ消息重试"),

    /**
     * 异步退款.
     */
    ASYNC_REFUND("ASYNC_REFUND","异步退款"),
    ;

    /**
     * JobTypeCode.
     */
    private final String jobTypeCode;

    /**
     * JobTypeDesc.
     */
    private final String jobTypeDesc;


    /**
     * 获取匹配JobType.
     *
     * @param jobTypeCode jobTypeCode.
     * @return jobType.
     */
    public static JobTypeEnum match(String jobTypeCode) {

        for (JobTypeEnum jobTypeEnum : JobTypeEnum.values()) {
            if (jobTypeEnum.getJobTypeCode().equals(jobTypeCode)) {
                return jobTypeEnum;
            }
        }

        return null;
    }
}
