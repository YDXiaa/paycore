package paydemo.biz.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.biz.route.RouteBiz;
import paydemo.common.PayStatusEnum;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.PayFundBO;

/**
 * @auther YDXiaa
 * <p>
 * 三方支付处理器.
 */
@Service
public class ThirdPayProcessor implements PayProcessor {

    @Autowired
    private RouteBiz routeBiz;


    @Override
    public RemotePayResult pay(PayFundBO payFundBO) {

        // mock返回结果
        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatusEnum(PayStatusEnum.FAIL);

        return remotePayResult;
    }

    @Override
    public RemotePayResult revoke(PayFundBO payFundBO) {
        return null;
    }
}
