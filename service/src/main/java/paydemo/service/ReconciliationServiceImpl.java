package paydemo.service;

import org.apache.dubbo.config.annotation.Service;
import paydemo.facade.ReconciliationServiceFacade;
import paydemo.facade.model.ErrorProcessRequestDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 对账结果处理.
 */
@Service
public class ReconciliationServiceImpl implements ReconciliationServiceFacade {

    @Override
    public Response<Boolean> errorProcess(ErrorProcessRequestDTO requestDTO) {
        return null;
    }
}
