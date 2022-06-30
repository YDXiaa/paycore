package paydemo.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @auther YDXiaa
 *
 * 支付主单BO.
 */
@Getter
@Setter
@ToString
public class PayOrderBO {

    /**
     * 请求订单号.
     */
    private String requestBizNo;

    /**
     * 订单请求时间.
     */
    private Date requestDate;

    /**
     * 业务线.
     */
    private String bizLine;

    /**
     * 支付单号.
     */
    private String payNo;

    /**
     * 支付金额.
     */
    private Long payAmt;

    /**
     * 退款金额.
     */
    private Long refundAmt;

    /**
     * 支付类型 PAY/REFUND/TRANSFER/RECHARGE.
     */
    private String payType;

    /**
     * 支付状态.
     */
    private String payStatus;

    /**
     * 支付创建时间.
     */
    private Date payCreateDate;

    /**
     * 支付完成时间.
     */
    private Date payCompletedDate;

    /**
     * 原支付单号.
     */
    private String origPayNo;

    /**
     * 备注.
     */
    private String orderRemark;

}
