package paydemo.facade.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 退款请求对象.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RefundRequestDTO extends PayBaseDTO {

    /**
     * 原业务请求号.
     */
    private String origRequestBizNo;

    /**
     * 原请求日期.
     */
    private Date origRequestDate;

    /**
     * 原订单来源业务线.
     */
    private String origBizLine;

    /**
     * 优先指定支付工具退款 默认优先从支付渠道.
     */
    private String refundPayTool;

    /**
     * 退款类型.
     */
    private String refundType;

    /**
     * 退款原因.
     */
    private String refundReason;
}
