package paydemo.facade;

import paydemo.facade.model.ErrorProcessRequestDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 差错处理.
 */
public interface ErrorServiceFacade {


    /**
     * 差错处理.
     *
     * @param requestDTO 请求对象.
     * @return 操作结果.
     */
    Response<Boolean> errorProcess(ErrorProcessRequestDTO requestDTO);


}
