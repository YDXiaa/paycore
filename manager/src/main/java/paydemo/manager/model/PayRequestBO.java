package paydemo.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import paydemo.common.RemotePayResult;

import java.util.Date;
import java.util.List;

/**
 * @auther YDXiaa
 * 业务请求BO.
 */
@Getter
@Setter
@ToString
public class PayRequestBO {

    /**
     * 请求日期业务号.
     */
    private String requestBizNo;

    /**
     * 请求日期.
     */
    private Date requestDate;

    /**
     * 业务线.
     */
    private String bizLine;

    /**
     * 支付类型.
     */
    private String payType;

    /**
     * 支付单号.
     */
    private String payNo;

    /**
     * 支付金额.
     */
    private long payAmt;

    /**
     * 退款金额.
     */
    private long refundAmt;

    /**
     * 支付状态.
     */
    private String payStatus;

    // ------------付款人信息--------------
    /**
     * 用户编号.
     */
    private String payerUserNo;

    /**
     * 平台登录号.
     */
    private String payerLoginNo;

    /**
     * 客户号.
     */
    private String payerCustomerNo;

    /**
     * 支付创建时间.
     */
    private Date payCreateDate;

    /**
     * 支付完成时间.
     */
    private Date payCompleteDate;

    /**
     * 资金单.
     */
    public List<PayFundBO> payFundBOList;

    /**
     * 最后一笔支付结果.
     */
    private RemotePayResult lastRemotePayResult;

    /**
     * 继续完成订单,不一定是成功订单.
     */
    private boolean continueCompletePayOrder;

}
