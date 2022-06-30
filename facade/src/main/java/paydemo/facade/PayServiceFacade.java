package paydemo.facade;

import paydemo.facade.model.PayResponseDTO;
import paydemo.facade.model.PayRequestDTO;
import paydemo.facade.model.Response;

/**
 * @auther YDXiaa
 * <p>
 * 支付接口.
 */
public interface PayServiceFacade {

    /**
     * 支付接口.
     *
     * @param payRequestDTO 支付请求对象.
     * @return 支付响应结果.
     */
    Response<PayResponseDTO> pay(PayRequestDTO payRequestDTO);

}
