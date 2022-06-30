package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 冲正标识.
 */
@Getter
@AllArgsConstructor
public enum RevokeFlagEnum {

    /**
     * 已冲正.
     */
    TRUE("TRUE", "已冲正"),

    /**
     * 未冲正.
     */
    FALSE("FALSE", "未冲正");

    /**
     * 冲正标识码.
     */
    private final String revokeFlagCode;

    /**
     * 冲正标识描述.
     */
    private final String revokeFlagDesc;
}
