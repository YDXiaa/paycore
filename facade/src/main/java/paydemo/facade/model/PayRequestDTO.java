package paydemo.facade.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @auther YDXiaa
 * <p>
 * 支付请求对象.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PayRequestDTO  extends PayBaseDTO {

    /**
     * 用户编号.
     */
    private String payerUserNo;

    /**
     * 平台登录号.
     */
    private String payerLoginNo;

    /**
     * 客户号.
     */
    private String payerCustomerNo;

    /**
     * 订单备注.
     */
    private String orderRemark;

    /**
     * 资金系统集合.
     */
    private List<PayFundRequestDTO> payFundRequestDTOList;

}
