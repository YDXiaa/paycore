package paydemo.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import paydemo.common.PaySubToolEnum;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 支付资金单BO.
 */
@Getter
@Setter
@ToString
public class PayFundBO implements Comparable<PayFundBO> {

    /**
     * 支付号.
     */
    private String payNo;

    /**
     * 支付资金单号.
     */
    private String payFundNo;

    /**
     * 支付类型.
     */
    private String payType;

    /**
     * 业务类型.
     */
    private String bizType;

    /**
     * 支付金额.
     */
    private long payAmt;

    /**
     * 已退款金额.
     */
    private long refundAmt;

    /**
     * 支付创建时间.
     */
    private Date payCreateDate;

    /**
     * 支付完成时间.
     */
    private Date payCompletedDate;

    /**
     * 支付工具.
     */
    private String payTool;

    /**
     * 支付子工具.
     */
    private String paySubTool;

    /**
     * 支付状态.
     */
    private String payStatus;

    /**
     * 通道编号.
     */
    private String channelNo;

    /**
     * 支付单号顺序
     */
    private long paySeq;

    /**
     * 冲正标识.
     */
    private String revokeFlag;

    /**
     * 结果码.
     */
    private String resultCode;

    /**
     * 结果描述.
     */
    private String resultDesc;

    @Override
    public int compareTo(PayFundBO payFundBO) {
        return PaySubToolEnum.getOrder(this.getPaySubTool()) - PaySubToolEnum.getOrder(payFundBO.getPaySubTool());
    }
}
