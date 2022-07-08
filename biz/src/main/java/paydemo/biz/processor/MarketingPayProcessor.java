package paydemo.biz.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.PayStatusEnum;
import paydemo.common.RemotePayResult;
import paydemo.common.VerifyUtil;
import paydemo.manager.db.PayFundManager;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.remote.marketing.MarketingRemoteService;

/**
 * @auther YDXiaa
 * <p>
 * 营销支付.
 */
@Service
public class MarketingPayProcessor implements PayProcessor {

    @Autowired
    private PayFundManager payFundManager;

    @Autowired
    private MarketingRemoteService marketingRemoteService;

    @Override
    public RemotePayResult pay(PayFundBO payFundBO) {

        payFundBO.setPayStatus(PayStatusEnum.PAYING.getStatusCode());
        // 更新支付状态为PAYING.
        int moidfyResult = payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.INIT);
        VerifyUtil.verifySqlResult(moidfyResult);

        RemotePayResult remotePayResult = marketingRemoteService.pay();

        // 设置支付单状态.
        payFundBO.setPayStatus(remotePayResult.getPayStatus());
        payFundBO.setOutRespSeqNo(remotePayResult.getOutRespSeqNo());
        payFundBO.setResultCode(remotePayResult.getResultCode());
        payFundBO.setResultDesc(remotePayResult.getResultDesc());

        // 更新支付结果.
        payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.PAYING);

        return remotePayResult;
    }

    @Override
    public RemotePayResult revoke(PayFundBO payFundBO) {
        return null;
    }
}
