package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 业务类型.
 */
@Getter
@AllArgsConstructor
public enum BizTypeEnum {

    /**
     * 移动H5模式.
     */
    MOB_H5("MOB_H5", "移动H5支付"),

    /**
     * 移动小程序(包括微信小程序、支付宝小程序).
     */
    MOB_MINI_APP("MOB_MINI_APP", "小程序"),

    /**
     * PC网站网关支付模式(跳转到支付宝网关收银台、微信支付动态商品二维码).
     */
    PC_ONLINE("PC_ONLINE", "PC网站网关支付"),
    ;


    /**
     * 业务类型编码.
     */
    private final String bizTypeCode;

    /**
     * 业务类型描述.
     */
    private final String bizTypeDesc;
}
