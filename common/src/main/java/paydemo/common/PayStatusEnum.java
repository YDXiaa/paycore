package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 支付状态枚举类.
 */
@Getter
@AllArgsConstructor
public enum PayStatusEnum {

    /**
     * 支付初始化状态.
     */
    INIT("INIT", "支付初始化"),

    /**
     * 支付中.
     */
    PAYING("PAYING", "支付中"),

    /**
     * 支付成功.
     */
    SUCCESS("SUCCESS", "支付成功"),

    /**
     * 支付失败.
     */
    FAIL("FAIL", "支付失败"),

    /**
     * 未支付直接关闭.
     */
    CLOSE("CLOSE", "关闭状态");


    /**
     * 状态编码.
     */
    private final String statusCode;

    /**
     * 状态描述.
     */
    private final String statusDesc;
}
