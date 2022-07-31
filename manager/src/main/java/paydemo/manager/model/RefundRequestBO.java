package paydemo.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auther YDXiaa
 * <p>
 * 退款请求BO.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class RefundRequestBO extends PayRequestBO {

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
     * 原支付单号.
     */
    private String origPayNo;

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

    /**
     * 退款原单资金单.
     */
    public List<PayFundBO> refundOrigPayFundBOList = new ArrayList<>();

    /**
     * 退款资金单.
     */
    public List<PayFundBO> refundFundBOList = new ArrayList<>();


}
