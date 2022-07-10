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
    public String payStatus;

    /**
     * 请求渠道流水号.
     */
    private String outRequestSeqNo;

    /**
     * 渠道返回流水号.
     */
    private String outRespSeqNo;

    /**
     * 渠道详细编码.
     */
    private String channelDetailNo;

    /**
     * 结果码.
     */
    private String resultCode;

    /**
     * 订单描述.
     */
    private String resultDesc;

    /**
     * 支付地址.
     */
    private String payUrl;

    /**
     * 是否是真实响应.
     */
    private boolean isRealRtn;

    /**
     * 拓展响应参数.
     */
    private Map<String, String> extResp;
}
