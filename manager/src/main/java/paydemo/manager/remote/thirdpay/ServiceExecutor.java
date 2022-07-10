package paydemo.manager.remote.thirdpay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.KeyCreator;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.RemoteRequestModel;

/**
 * @auther YDXiaa
 * <p>
 * 服务执行器.
 */
@Slf4j
@Service
public class ServiceExecutor {

    @Autowired
    private ThirdPayServiceFactory payServiceFactory;

    /**
     * 远程调用.
     *
     * @param requestModel 远程调用参数.
     * @return 执行结果.
     */
    public RemotePayResult executeRemoteInvoke(RemoteRequestModel requestModel) {

        String serviceKey = KeyCreator.createKey(requestModel.getClassPackageName(), requestModel.getMethodName());

        return payServiceFactory.getServiceAdapter(serviceKey).service(requestModel);
    }


}
