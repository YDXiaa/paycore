package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 差错处理类型.
 */
@Getter
@AllArgsConstructor
public enum ErrorProcessTypeEnum {

    /**
     * 人工核查.
     */
    MANUAL("MANUAL", "挂起人工核查"),

    /**
     * 重试处理(交易场景,不允许重试).
     */
    RETRY("RETRY", "重试处理"),

    /**
     * 发起查询.
     */
    QUERY_RESULT("QUERY_RESULT", "发起查询"),

    /**
     * 挂单,等待对账平台处理.
     */
    WAIT_RECON("WAIT_RECON", "等待对账处理"),

    /**
     * 关闭差错.
     */
    CLOSE("CLOSE", "关闭");


    /**
     * 差错处理类型编码.
     */
    private String errorProcessTypeCode;

    /**
     * 差错处理类型描述
     */
    private String errorProcessTypeDesc;
}
