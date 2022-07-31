package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * redisKey规则：当前系统_业务名称
 */
@Getter
@AllArgsConstructor
public enum RedisKeyEnum {

    /**
     * 支付业务key.
     */
    PAYCORE_PAY("PAYCORE_PAY", "支付业务"),

    /**
     * 退款业务key.
     */
    PAYCORE_REFUND("PAYCORE_REFUND", "退款业务"),

    /**
     * 支付单号key.
     */
    PAYCORE_PAYNO("PAYCORE_PAY_NO", "支付单号生成"),

    /**
     * 渠道请求流水号.
     */
    PAYCORE_CHANNEL_SEQ_NO("PAYCORE_CHANNEL_SEQ_NO", "渠道请求流水号"),

    /**
     * 支付资金单号key.
     */
    PAYCORE_PAY_FUND_NO("PAYCORE_PAY_FUND_NO", "支付资金单单号生成"),

    /**
     * 渠道成功率.
     */
    PAYCORE_SUCCESS_RATE("PAYCORE_SUCCESS_RATE", "渠道成功率"),

    /**
     * 支付成功数量subKey.
     */
    SUCCESS_CNT("SUCCESS_CNT", "支付成功笔数subKey"),

    /**
     * 支付成功数量subKey.
     */
    PAYING_CNT("PAYING_CNT", "支付中笔数subKey"),

    /**
     * 支付失败数量subKey.
     */
    FAIL_CNT("FAIL_CNT", "支付失败笔数subKey");

    /**
     * 前缀编码.
     */
    private final String redisKeyCode;

    /**
     * 前缀描述.
     */
    private final String redisKeyDesc;

}
