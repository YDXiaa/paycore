package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 支付工具大类枚举类.
 */
@Getter
@AllArgsConstructor
public enum PayToolEnum {

    /**
     * 用户余额(用户账户可以是自建的钱包需要专门牌照例如预付卡或者对接持牌机构的资管系统).
     */
    BALANCE("BALANCE", "余额账户"),

    /**
     * 第三方支付工具包括直连第三方支付和通过聚合服务商接入.
     */
    THIRD_PAY("THIRD_PAY", "第三方支付"),

    /**
     * 营销支付，包括积分、代金券等,基本形式参考微信运营工具: https://pay.weixin.qq.com/static/product/product_intro.shtml?name=points
     */
    MARKETING("MARKETING", "营销能力支付");

    /**
     * 支付工具编码.
     */
    private final String payToolCode;

    /**
     * 支付工具描述.
     */
    private final String payToolDesc;
}
