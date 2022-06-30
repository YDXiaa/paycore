package paydemo.common;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 支付子工具枚举类.
 */
@Getter
@AllArgsConstructor
public enum PaySubToolEnum {

    /**
     * 红包账户.
     */
    BALANCE("BALANCE", "余额账户", PayToolEnum.BALANCE, 10),


    /**
     * 积分支付.
     */
    POINTS("POINTS", "积分", PayToolEnum.MARKETING, 11),

    /**
     * 代金券支付(例如品类券、商品券).
     */
    COUPON("COUPON", "代金券", PayToolEnum.MARKETING, 12),

    /**
     * 支付宝.
     */
    ALIPAY("ALIPAY", "支付宝", PayToolEnum.THIRD_PAY, 20),

    /**
     * 花呗信用支付.
     */
    ANT_CREDIT_PAY("ANT_CREDIT_PAY", "花呗信用支付", PayToolEnum.THIRD_PAY, 21),

    /**
     * 微信支付.
     */
    WECHATPAY("WECHATPAY", "微信", PayToolEnum.THIRD_PAY, 22),

    /**
     * qq支付.
     */
    QQPAY("QQPAY", "qq支付", PayToolEnum.THIRD_PAY, 23),

    /**
     * 银联云闪付.
     */
    UNIONPAY("UNIONPAY", "银联云闪付", PayToolEnum.THIRD_PAY, 24),

    /**
     * apple支付.
     */
    APPLEPAY("APPLEPAY", "applePay", PayToolEnum.THIRD_PAY, 25),

    /**
     * 京东支付.
     */
    JDPAY("JDPAY", "京东支付", PayToolEnum.THIRD_PAY, 26),

    ;


    /**
     * 支付子工具编码.
     */
    private final String paySubToolCode;

    /**
     * 支付子工具描述.
     */
    private final String paySubToolDesc;

    /**
     * 支付工具大类
     */
    private final PayToolEnum payToolEnum;

    /**
     * 执行顺序.
     */
    private final int executeOrder;

    /**
     * 获取支付顺序.
     *
     * @param paySubToolCode 支付子类型编码.
     * @return 顺序.
     */
    public static int getOrder(String paySubToolCode) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(paySubToolCode), "paySubToolCode must be not NULL");

        for (PaySubToolEnum paySubToolEnum : PaySubToolEnum.values()) {
            if (paySubToolEnum.getPaySubToolCode().equals(paySubToolCode)) {
                return paySubToolEnum.getExecuteOrder();
            }
        }

        // todo 异常.
        return 0;
    }
}
