package paydemo.facade;

import paydemo.facade.model.PayQueryRequestDTO;
import paydemo.facade.model.PayResponseDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 支付查询.
 */
public interface PayQueryServiceFacade {


    /**
     * 支付查询.
     *
     * @param requestDTO 请求对象.
     * @return 支付查询响应对象.
     */
    Response<PayResponseDTO> payQuery(PayQueryRequestDTO requestDTO);

}
