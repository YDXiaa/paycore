package paydemo.facade.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @auther YDXiaa
 * <p>
 * 支付请求对象.
 */
@Data
public class PayRequestDTO implements Serializable {

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

    /**
     * 订单备注.
     */
    private String orderRemark;

    /**
     * 资金系统集合.
     */
    private List<PayFundRequestDTO> payFundRequestDTOList;


}
