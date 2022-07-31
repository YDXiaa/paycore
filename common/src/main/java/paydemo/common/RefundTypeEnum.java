package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 退款类型.
 */
@Getter
@AllArgsConstructor
public enum RefundTypeEnum {

    /**
     * 退回到原支付工具上。
     */
    REFUND_ORIG_PAY_TOOL("REFUND_ORIG_PAY_TOOL", "退回到原支付工具上"),

    /**
     * 退回到支付账户中。
     */
    REFUND_BALANCE("REFUND_BALANCE", "退回到支付账户中"),

    ;

    /**
     * 退款类型编码.
     */
    private final String refundTypeCode;

    /**
     * 退款类型描述.
     */
    private final String refundTypeDesc;

}
