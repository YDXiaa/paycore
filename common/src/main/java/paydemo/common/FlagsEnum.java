package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 标识枚举类.
 */
@Getter
@AllArgsConstructor
public enum FlagsEnum {

    /**
     * TRUE.
     */
    TRUE("TRUE", "TRUE"),

    /**
     * FALSE.
     */
    FALSE("FALSE", "FALSE");

    private final String flagCode;

    private final String flagDesc;
}
