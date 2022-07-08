package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 统一开关.
 */
@Getter
@AllArgsConstructor
public enum SwitchStatusEnum {

    /**
     * OPEN.
     */
    OPEN("OPEN", "打开"),

    /**
     * CLOSE.
     */
    CLOSE("CLOSE", "关闭");

    /**
     * 开关状态编码.
     */
    private final String switchStatusCode;

    /**
     * 开关状态描述.
     */
    private final String switchStatusDesc;

}
