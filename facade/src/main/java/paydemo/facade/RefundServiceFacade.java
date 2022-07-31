package paydemo.facade;

import paydemo.facade.model.PayResponseDTO;
import paydemo.facade.model.RefundRequestDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 退款服务.
 */
public interface RefundServiceFacade {

    /**
     * 退款.
     *
     * @param requestDTO 请求对象.
     * @return 响应对象.
     */
    Response<PayResponseDTO> refund(RefundRequestDTO requestDTO);

}
