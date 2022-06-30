package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 业务线.
 */
@Getter
@AllArgsConstructor
public enum BizLineEnum {

    /**
     * C端线上商城.
     */
    MALL("MALL", "线上商城", "0000"),

    /**
     * B端供应链.
     */
    SCM("SCM", "供应链业务", "0001"),

    /**
     * 直播业务.
     */
    LIVE("LIVE", "直播业务", "0003"),

    /**
     * second-hand二手交易市场.
     */
    SH_TRANS("SH_TRANS", "二手交易", "0004");


    /**
     * 业务线编码.
     */
    private final String bizLineName;

    /**
     * 业务线描述.
     */
    private final String bizLineDesc;

    /**
     * 业务编码.
     */
    private final String bizCode;

    public static String getBizCode(String bizLineName) {
        return MALL.getBizCode();
    }
}
