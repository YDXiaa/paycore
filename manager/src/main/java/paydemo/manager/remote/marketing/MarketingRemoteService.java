package paydemo.manager.remote.marketing;

import org.springframework.stereotype.Service;
import paydemo.common.PayStatusEnum;
import paydemo.common.RemotePayResult;

/**
 * @auther YDXiaa
 * <p>
 * 券核销.
 */
@Service
public class MarketingRemoteService {

    public RemotePayResult pay() {
        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
        return remotePayResult;
    }

}
