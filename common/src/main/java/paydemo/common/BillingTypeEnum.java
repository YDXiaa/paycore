package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 计费类型.
 */
@Getter
@AllArgsConstructor
public enum BillingTypeEnum {

    /**
     * 固定计费.
     */
    FIXED("FIXED", "固定计费"),

    /**
     * 按照比率计费.
     */
    RATE("RATE", "比率计费"),
    ;

    /**
     * 计费类型编码.
     */
    private final String billingTypeCode;

    /**
     * 计费类型描述.
     */
    private final String billingTypeDesc;
}
