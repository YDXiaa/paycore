package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 差错处理方式.
 */
@Getter
@AllArgsConstructor
public enum ErrorProcessStatusEnum {

    /**
     * 等待处理.
     */
    WAIT_PROCESS("WAIT_PROCESS", "等待处理"),

    /**
     * 处理成功.
     */
    SUCCESS("SUCCESS","处理成功"),

    /**
     * 关闭.
     */
    CLOSE("CLOSE", "关闭");

    /**
     * 差错处理状态编码.
     */
    private String errorProcessStatusCode;

    /**
     * 差错处理状态描述.
     */
    private String errorProcessStatusDesc;
}
