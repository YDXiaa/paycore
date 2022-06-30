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
     * 支付单号key.
     */
    PAYCORE_PAYNO("PAYCORE_PAY_NO", "支付单号生成"),

    /**
     * 支付资金单号key.
     */
    PAYCORE_PAY_FUND_NO("PAYCORE_PAY_FUND_NO", "支付资金单单号生成"),

    ;

    /**
     * 前缀编码.
     */
    private final String redisKeyCode;

    /**
     * 前缀描述.
     */
    private final String redisKeyDesc;

}
