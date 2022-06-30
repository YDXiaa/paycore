package paydemo.facade.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther YDXiaa
 * 支付公共响应对象.
 */
@Data
public class PayResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付状态.
     */
    private String payStatus;

    /**
     * 支付订单号.
     */
    private String payNo;

    /**
     * 支付创建时间.
     */
    private Date payCreateDate;

    /**
     * 支付完成时间.
     */
    private Date payCompleteDate;

    /**
     * 响应码.
     */
    private String respCode;

    /**
     * 响应信息.
     */
    private String respMsg;
}
