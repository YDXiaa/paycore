package paydemo.facade.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 支付基础请求对象.
 */
@Data
public class PayBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 外部系统请求业务号.
     */
    private String requestBizNo;

    /**
     * 请求日期.
     */
    private Date requestDate;

    /**
     * 订单来源业务线.
     */
    private String bizLine;

    /**
     * 支付金额.
     */
    private long payAmt;

    /**
     * 支付类型.
     */
    private String payType;
}
