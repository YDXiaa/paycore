package paydemo.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther YDXiaa
 * <p>
 * payFundRequestDTO.
 */
@Data
public class PayFundRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付金额.
     */
    private long payAmt;

    /**
     * 支付工具大类.
     */
    private String payTool;

    /**
     * 支付工具子类.
     */
    private String paySubTool;

    /**
     * 支付类型.
     */
    private String payType;

    /**
     * 业务类型.
     */
    private String bizType;

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

}
