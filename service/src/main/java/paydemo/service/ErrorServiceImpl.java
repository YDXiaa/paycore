package paydemo.service;

import org.apache.dubbo.config.annotation.Service;
import paydemo.facade.ErrorServiceFacade;
import paydemo.facade.model.ErrorProcessRequestDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 差错处理.
 */
@Service
public class ErrorServiceImpl implements ErrorServiceFacade {

    @Override
    public Response<Boolean> errorProcess(ErrorProcessRequestDTO requestDTO) {
        return null;
    }
}
