package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 差错类型.
 */
@Getter
@AllArgsConstructor
public enum ErrorTypeEnum {

    /**
     * 内部冲正失败.
     */
    REVOKE_FAIL("REVOKE_FAIL", "内部冲正失败"),

    ;
    /**
     * 差错类型编码.
     */
    private String errorTypeCode;

    /**
     * 差错类型描述.
     */
    private String errorTypeDesc;
}
