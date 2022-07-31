package paydemo.facade.mq;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 支付通知message.
 */
@Data
@Builder
public class PayNoticeMessage implements Serializable {

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
     * 交易创建日期.
     */
    private Date payCreateDate;

    /**
     * 交易完成日期
     */
    private Date payCompletedDate;

    /**
     * 支付状态.
     */
    private String payStatus;

    /**
     * 结果码.
     */
    private String resultCode;

    /**
     * 结果描述.
     */
    private String resultDesc;


}
