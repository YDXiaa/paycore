package paydemo.dao.dbmodel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @auther YDXiaa
 * <p>
 * 渠道计费.
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("T_CHANNEL_DETAIL_FEE")
public class ChannelDetailFeeDO extends BaseDO {

    /**
     * 渠道编号.
     */
    @TableField("channel_no")
    private String channelNo;

    /**
     * 渠道详细编号.
     */
    @TableField("channel_detail_no")
    private String channelDetailNo;

    /**
     * 收费模式:单笔计费 SINGLE
     */
    @TableField("fee_cal_mode")
    private String feeCalMode;

    /**
     * 计费模式 固定收费(FIXED)、比率收费(RATE) .
     */
    @TableField("billing_type")
    private String billingType;

    /**
     * 计费值.
     */
    @TableField("billing_value")
    private BigDecimal billingValue;

    /**
     * 计费开始金额(交易额多少才开始计费).
     */
    @TableField("fee_begin_amt")
    private Long feeBeginAmt;

    /**
     * 计费结束金额(交易额最高才结束计费).
     */
    @TableField("fee_end_amt")
    private Long feeEndAmt;

    /**
     * 手续费收取最低金额.
     */
    @TableField("fee_min_amt")
    private Long feeMinAmt;

    /**
     * 手续费收取最大金额.
     */
    @TableField("fee_max_amt")
    private Long feeMaxAmt;

    /**
     * 计算精度当为小数时候如何处理精度.
     */
    @TableField("fee_cal_accuracy")
    private String feeCalAccuracy;


}
