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

    /**
     * 撤销.
     */
    CANCEL("CANCEL", "撤销", "03"),

    /**
     * 代扣.
     */
    WITHHOLD("WITHHOLD", "代扣", "04"),

    /**
     * 冲正.
     */
    REVOKE("REVOKE", "冲正", "05"),

    /**
     * 代发.
     */
    ISSUE("ISSUE", "代发", "06"),

    /**
     * 认证.
     */
    AUTH("AUTH", "认证", "07"),

    /**
     * 转账.
     */
    TRANSFER("TRANSFER","转账","08")

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
