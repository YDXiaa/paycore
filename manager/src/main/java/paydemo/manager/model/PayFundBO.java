package paydemo.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import paydemo.common.PaySubToolEnum;
import paydemo.util.PayStatusEnum;

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
     * 原支付资金单号.
     */
    private String origPayFundNo;

    /**
     * 支付金额.
     */
    private long payAmt;

    /**
     * 已退款金额.
     */
    private long refundAmt;

    /**
     * 渠道手续费.
     */
    private long channelFeeAmt;

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
     * 外部授权userId.
     */
    private String extAuthUserId;

    /**
     * 代金券号.
     */
    private String couponNo;

    /**
     * 客户关联账户编号.
     */
    private String payerCustomerAccNo;

    /**
     * 账户分类.
     */
    private String payerAccClassify;

    /**
     * 客户类型.
     */
    private String payerAccType;

    /**
     * 账户号.
     */
    private String payerAccNo;

    /**
     * 代扣场景下，代扣协议号.
     */
    private String agreementNo;

    /**
     * 支付状态.
     */
    private String payStatus;

    /**
     * 原状态.
     */
    private PayStatusEnum origPayStatus;

    /**
     * 渠道编号.
     */
    private String channelDetailNo;

    /**
     * 外部请求流水号.
     */
    private String outRequestSeqNo;

    /**
     * 外部响应流水号.
     */
    private String outRespSeqNo;

    /**
     * 全路径包名.
     */
    private String classPackageName;

    /**
     * 类方法名.
     */
    private String methodName;

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
