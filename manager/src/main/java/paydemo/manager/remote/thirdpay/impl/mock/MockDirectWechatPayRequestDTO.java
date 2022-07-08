package paydemo.manager.remote.thirdpay.impl.mock;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther YDXiaa
 * <p>
 * mockRequest.
 */
@Data
public class MockDirectWechatPayRequestDTO implements Serializable {

    /**
     * 支付金额.
     */
    private Long payAmt;

}
