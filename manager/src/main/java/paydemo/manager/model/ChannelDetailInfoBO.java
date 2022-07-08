package paydemo.manager.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 渠道详情信息.
 */
@Data
public class ChannelDetailInfoBO{

    /**
     * 渠道编码(例如支付宝 ALIPAY).
     */
    private String channelNo;

    /**
     * 渠道自定义名称.
     */
    private String channelName;

    /**
     * 渠道路由比例(新通道开启比例) 渠道排序的时候使用 强制使用其他排序因子失效.
     */
    private Long channelWeight;

    /**
     * 渠道不服务原因(用于收银台展示).
     */
    private String noServiceDesc;

    /**
     * 渠道不服务开始时间.
     */
    private Date noServiceBeginTime;

    /**
     * 渠道不服务结束时间.
     */
    private Date noServiceEndTime;

    /**
     * 渠道状态 OPEN/CLOSE (收银台不展示，收银台展示使用noServiceFlag判断).
     */
    private String channelStatus;

    /**
     * 渠道详情编号(渠道编号+支付类型).
     */
    private String channelDetailNo;

    /**
     * 渠道详细名称.
     */
    private String channelDetailName;

    /**
     * 支付类型.
     */
    private String payType;

    /**
     * 支付业务类型.
     */
    private String bizType;

    /**
     * 支付工具类型.
     */
    private String payTool;

    /**
     * 支付工具子类型.
     */
    private String paySubTool;

    /**
     * 最低金额.
     */
    private Long minPayAmt;

    /**
     * 最大金额.
     */
    private Long maxPayAmt;

    /**
     * 渠道流水号长度.
     */
    private Long channelSeqLen;

    /**
     * 全路径包名.
     */
    private String classPackageName;

    /**
     * 类方法名.
     */
    private String methodName;

    /**
     * 计费标识,TRUE计费,FALSE不计费.
     */
    private String feeFlag;

    /**
     * 渠道营销折扣标识.
     */
    private String discountFlag;

    /**
     * 收费模式:单笔计费 SINGLE
     */
    private String feeCalMode;

    /**
     * 计费模式 固定收费(FIXED)、比率收费(RATE) .
     */
    private String billingType;

    /**
     * 计费值.
     */
    private BigDecimal billingValue;

    /**
     * 计费开始金额(交易额多少才开始计费).
     */
    private Long feeBeginAmt;

    /**
     * 计费结束金额(交易额最高才结束计费).
     */
    private Long feeEndAmt;

    /**
     * 手续费收取最低金额.
     */
    private Long feeMinAmt;

    /**
     * 手续费收取最大金额.
     */
    private Long feeMaxAmt;

    /**
     * 计算精度当为小数时候如何处理精度.
     */
    private String feeCalAccuracy;

    /**
     * 渠道详情状态 OPEN/CLOSE.
     */
    private String channelDetailStatus;

    /**
     * 通道收取手续费.
     */
    private Long channelFeeAmt;

    /**
     * 手续费占比(double类型).
     */
    private double feeAmtProportion;

    /**
     * 渠道计算的的得到权重分数.
     */
    private double channelDetailScore;
}
