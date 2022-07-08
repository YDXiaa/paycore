package paydemo.facade.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @auther YDXiaa
 * <p>
 * 渠道收费信息.
 */
@Data
public class ChannelDetailFeeRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

}
