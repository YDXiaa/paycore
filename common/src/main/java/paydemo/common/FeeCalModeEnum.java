package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * 手续费计算模式.
 */
@Getter
@AllArgsConstructor
public enum FeeCalModeEnum {

    /**
     * 单笔计费.
     */
    SINGLE("SINGLE", "单笔计费"),
    ;

    /**
     * 手续费计算模式编码.
     */
    private final String feeCalModeCode;

    /**
     * 手续费计算模式描述.
     */
    private final String feeCalModeDesc;
}
