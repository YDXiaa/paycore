package paydemo.biz.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.remote.marketing.MarketingRemoteService;
import paydemo.util.PayStatusEnum;

/**
 * @auther YDXiaa
 * <p>
 * 营销支付.
 */
@Service
public class MarketingPayProcessor extends BasePayProcess implements PayProcessor {

    @Autowired
    private MarketingRemoteService marketingRemoteService;

    @Override
    public RemotePayResult pay(PayFundBO payFundBO) {

        payBeforePayStatusProcess(payFundBO);

        RemotePayResult remotePayResult = marketingRemoteService.pay();

        // 设置支付单状态.
        payFundBO.setPayStatus(remotePayResult.getPayStatus());
        payFundBO.setOutRespSeqNo(remotePayResult.getOutRespSeqNo());
        payFundBO.setResultCode(remotePayResult.getResultCode());
        payFundBO.setResultDesc(remotePayResult.getResultDesc());

        payResultStatusProcess(payFundBO);

        return remotePayResult;
    }

    @Override
    public RemotePayResult payQuery(PayFundBO payFundBO) {
        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
        remotePayResult.setOutRequestSeqNo(payFundBO.getOutRequestSeqNo());
        return remotePayResult;
    }

    @Override
    public RemotePayResult revoke(PayFundBO payFundBO) {

        payBeforePayStatusProcess(payFundBO);

        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
        remotePayResult.setOutRequestSeqNo(payFundBO.getOutRequestSeqNo());

        // 设置支付单状态.
        payFundBO.setPayStatus(remotePayResult.getPayStatus());
        payFundBO.setOutRespSeqNo(remotePayResult.getOutRespSeqNo());
        payFundBO.setResultCode(remotePayResult.getResultCode());
        payFundBO.setResultDesc(remotePayResult.getResultDesc());

        payResultStatusProcess(payFundBO);

        return remotePayResult;
    }

    @Override
    public RemotePayResult revokeQuery(PayFundBO payFundBO) {
        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
        remotePayResult.setOutRequestSeqNo(payFundBO.getOutRequestSeqNo());
        return remotePayResult;
    }
}
