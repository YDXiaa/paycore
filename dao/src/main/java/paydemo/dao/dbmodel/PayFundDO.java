package paydemo.dao.dbmodel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 支付资金单.
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("T_PAY_FUND")
public class PayFundDO extends BaseDO {

    /**
     * 支付单号.
     */
    @TableField("pay_no")
    private String payNo;

    /**
     * 支付资金单号.
     */
    @TableField("pay_fund_no")
    private String payFundNo;

    /**
     * 支付创建日期.
     */
    @TableField("pay_create_date")
    private Date payCreateDate;

    /**
     * 支付完成日期.
     */
    @TableField("pay_completed_date")
    private Date payCompletedDate;

    /**
     * 原支付资金单号.
     */
    @TableField("orig_pay_fund_no")
    private Long origPayFundNo;

    /**
     * 外部请求流水号.
     */
    @TableField("out_request_seq_no")
    private String outRequestSeqNo;

    /**
     * 外部响应流水号.
     */
    @TableField("out_resp_seq_no")
    private String outRespSeqNo;

    /**
     * 支付类型.
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 支付金额.
     */
    @TableField("pay_amt")
    private Long payAmt;

    /**
     * 已退金额.
     */
    @TableField("refund_amt")
    private Long refundAmt;

    /**
     * 渠道手续费.
     */
    @TableField("channel_fee_amt")
    private Long channelFeeAmt;

    /**
     * 支付工具类型.
     */
    @TableField("pay_tool")
    private String payTool;

    /**
     * 支付工具子类.
     */
    @TableField("pay_sub_tool")
    private String paySubTool;

    /**
     * 支付单序号.
     */
    @TableField("pay_seq")
    private Long paySeq;

    /**
     * 渠道详情编号.
     */
    @TableField("channel_detail_no")
    private String channelDetailNo;

    /**
     * 支付状态.
     */
    @TableField("pay_status")
    private String payStatus;

    /**
     * 冲正标识 TRUE已冲正,FALSE 未冲正.
     */
    @TableField("revoke_flag")
    private String revokeFlag;

}
