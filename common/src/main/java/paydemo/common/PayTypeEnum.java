package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 支付类型.
 */
@Getter
@AllArgsConstructor
public enum PayTypeEnum {

    /**
     * 支付.
     */
    PAY("PAY", "支付", "01"),

    /**
     * 退款.
     */
    REFUND("REFUND", "退款", "02"),
    ;

    /**
     * 支付类型编码.
     */
    private final String payTypeCode;

    /**
     * 支付类型描述.
     */
    private final String payTypeDesc;

    /**
     * 交易内部编码.
     */
    private final String transCode;


    /**
     * 获取支付服务编码.
     *
     * @param payTypeCode 支付类型code.
     * @return 支付服务编码.
     */
    public static String getTransCode(String payTypeCode) {

        VerifyUtil.verifyRequiredField(payTypeCode);

        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            if (payTypeEnum.getPayTypeCode().equals(payTypeCode)) {
                return payTypeEnum.getTransCode();
            }
        }

        return null;
    }
}
