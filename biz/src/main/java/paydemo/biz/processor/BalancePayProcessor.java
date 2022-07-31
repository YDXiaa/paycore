package paydemo.biz.processor;

import org.springframework.stereotype.Service;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.PayFundBO;
import paydemo.util.PayStatusEnum;


/**
 * @auther YDXiaa
 * <p>
 * 余额支付处理器.
 */
@Service
public class BalancePayProcessor implements PayProcessor {


    @Override
    public RemotePayResult pay(PayFundBO payFundBO) {

        // mock返回结果
        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());

        return remotePayResult;
    }

    @Override
    public RemotePayResult payQuery(PayFundBO payFundBO) {
        return null;
    }

    @Override
    public RemotePayResult revoke(PayFundBO payFundBO) {
        return null;
    }

    @Override
    public RemotePayResult revokeQuery(PayFundBO payFundBO) {
        return null;
    }
}
