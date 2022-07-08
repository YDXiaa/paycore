package paydemo.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 业务响应BO.
 */
@Getter
@Setter
@ToString
public class PayResponseBO {

    /**
     * 支付单号.
     */
    private String payNo;

    /**
     * 交易创建日期.
     */
    private Date payCreateDate;

    /**
     * 交易完成日期
     */
    private Date payCompletedDate;

    /**
     * 渠道号.
     */
    private String channelDetailNo;

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

    /**
     * 冲正标识.
     */
    private boolean revokeFlag;

}
