package paydemo.facade;

import paydemo.facade.model.ErrorProcessRequestDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 对账处理.
 */
public interface ReconciliationServiceFacade {


    /**
     * 对账差错信息处理.
     *
     * @param requestDTO 请求对象.
     * @return 处理结果.
     */
    Response<Boolean> errorProcess(ErrorProcessRequestDTO requestDTO);
}
