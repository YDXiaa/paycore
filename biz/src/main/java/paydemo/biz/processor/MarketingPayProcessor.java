package paydemo.biz.processor;

import org.springframework.stereotype.Service;
import paydemo.common.PayStatusEnum;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.PayFundBO;

/**
 * @auther YDXiaa
 *
 * 营销支付.
 */
@Service
public class MarketingPayProcessor implements PayProcessor{

    @Override
    public RemotePayResult pay(PayFundBO payFundBO) {
        // mock返回结果
        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatusEnum(PayStatusEnum.SUCCESS);

        return remotePayResult;
    }

    @Override
    public RemotePayResult revoke(PayFundBO payFundBO) {
        return null;
    }
}
