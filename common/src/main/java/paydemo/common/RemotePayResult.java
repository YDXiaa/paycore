package paydemo.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @auther YDXiaa
 * <p>
 * 支付结果.
 */
@Getter
@Setter
@ToString
public class RemotePayResult {

    /**
     * 支付状态.
     */
    public PayStatusEnum payStatusEnum;

    /**
     * 结果码.
     */
    private String resultCode;

    /**
     * 订单描述.
     */
    private String resultDesc;

    /**
     * 是否是真实响应.
     */
    private boolean isRealRtn;

    /**
     * 拓展响应参数.
     */
    private Map<String, String> extResp;
}
