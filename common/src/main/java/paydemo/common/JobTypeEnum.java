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
    PAY_REVOKE("PAY_REVOKE", "支付冲正");

    /**
     * JobTypeCode.
     */
    private final String jobTypeCode;

    /**
     * JobTypeDesc.
     */
    private final String jobTypeDesc;
}
