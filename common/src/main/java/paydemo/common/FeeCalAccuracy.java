package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 计费精度.
 */
@Getter
@AllArgsConstructor
public enum FeeCalAccuracy {

    /**
     * 四舍五入.
     */
    ROUND("ROUND", "四舍五入"),
    ;

    /**
     * 计费精度编码.
     */
    private final String feeCalAccuracyCode;

    /**
     * 计费精度描述.
     */
    private final String feeCalAccuracyDesc;
}
