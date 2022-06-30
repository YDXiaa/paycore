package paydemo.dao.dbmodel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @auther YDXiaa
 * 支付单.
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("T_PAY_ORDER")
public class PayOrderDO extends BaseDO {

    /**
     * 请求订单号.
     */
    @TableField("request_biz_no")
    private String requestBizNo;

    /**
     * 订单请求时间.
     */
    @TableField("request_date")
    private Date requestDate;

    /**
     * 业务线.
     */
    @TableField("biz_line")
    private String bizLine;

    /**
     * 支付单号.
     */
    @TableField("pay_no")
    private String payNo;

    /**
     * 支付金额.
     */
    @TableField("pay_amt")
    private Long payAmt;

    /**
     * 退款金额.
     */
    @TableField("refund_amt")
    private Long refundAmt;

    /**
     * 支付类型 PAY/REFUND/TRANSFER/RECHARGE.
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 支付状态.
     */
    @TableField("pay_status")
    private String payStatus;

    /**
     * 支付创建时间.
     */
    @TableField("pay_create_date")
    private Date payCreateDate;

    /**
     * 支付完成时间.
     */
    @TableField("pay_completed_date")
    private Date payCompletedDate;

    /**
     * 原支付单号.
     */
    @TableField("orig_pay_no")
    private String origPayNo;

    /**
     * 备注.
     */
    @TableField("order_remark")
    private String orderRemark;

}
