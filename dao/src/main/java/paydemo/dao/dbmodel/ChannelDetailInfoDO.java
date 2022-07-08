package paydemo.dao.dbmodel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @auther YDXiaa
 * <p>
 * 渠道详情信息.
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("T_CHANNEL_DETAIL_INFO")
public class ChannelDetailInfoDO extends BaseDO {

    /**
     * 渠道编号.
     */
    @TableField("channel_no")
    private String channelNo;

    /**
     * 渠道详情编号(渠道编号+支付类型).
     */
    @TableField("channel_detail_no")
    private String channelDetailNo;

    /**
     * 渠道详细名称.
     */
    @TableField("channel_detail_name")
    private String channelDetailName;

    /**
     * 支付类型.
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 支付业务类型.
     */
    @TableField("biz_type")
    private String bizType;

    /**
     * 渠道请求流水号长度.
     */
    @TableField("channel_seq_len")
    private Long channelSeqLen;

    /**
     * 支付工具类型.
     */
    @TableField("pay_tool")
    private String payTool;

    /**
     * 支付工具子类型.
     */
    @TableField("pay_sub_tool")
    private String paySubTool;

    /**
     * 最低金额.
     */
    @TableField("min_pay_amt")
    private Long minPayAmt;

    /**
     * 最大金额.
     */
    @TableField("max_pay_amt")
    private Long maxPayAmt;

    /**
     * 全路径包名.
     */
    @TableField("class_package_name")
    private String classPackageName;

    /**
     * 类方法名.
     */
    @TableField("method_name")
    private String methodName;

    /**
     * 计费标识,TRUE计费,FALSE不计费.
     */
    @TableField("fee_flag")
    private String feeFlag;

    /**
     * 渠道营销折扣标识.
     */
    @TableField("discount_flag")
    private String discountFlag;

    /**
     * 渠道详情状态 OPEN/CLOSE.
     */
    @TableField("channel_detail_status")
    private String channelDetailStatus;

}
