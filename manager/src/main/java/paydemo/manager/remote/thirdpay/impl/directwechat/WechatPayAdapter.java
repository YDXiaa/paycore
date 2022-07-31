package paydemo.manager.remote.thirdpay.impl.directwechat;

import org.springframework.stereotype.Service;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.manager.remote.thirdpay.BaseRemoteServiceAdapter;
import paydemo.manager.remote.thirdpay.DubboServiceRegistry;
import paydemo.paygateway.facade.model.GatewayPayResponse;
import paydemo.paygateway.facade.model.pay.DirectWeChatPayRequestDTO;
import paydemo.paygateway.facade.pay.DirectWeChatPayFacade;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 微信支付订单退款.
 */
@Service
public class WechatPayAdapter extends DubboServiceRegistry implements BaseRemoteServiceAdapter<DirectWeChatPayRequestDTO,
        GatewayPayResponse> {

    @Override
    public DirectWeChatPayRequestDTO createServiceReq(RemoteRequestModel model) {

        DirectWeChatPayRequestDTO requestDTO = new DirectWeChatPayRequestDTO();
        requestDTO.setPayAmt(model.getPayAmt());
        requestDTO.setChannelRequestSeqNo(model.getOutRequestSeqNo());
        return requestDTO;
    }

    @Override
    public GatewayPayResponse doService(DirectWeChatPayRequestDTO request) {

        try {
            Response<GatewayPayResponse> gwResponse = getDubboService(DirectWeChatPayFacade.class).pay(request);
            return gwResponse.getRespData();
        } catch (Throwable throwable) {
            return invokeException(throwable, false);
        }
    }

    @Override
    public RemotePayResult createServiceResp(RemoteRequestModel model, GatewayPayResponse response) {

        RemotePayResult remotePayResult = new RemotePayResult();

        remotePayResult.setPayStatus(response.getPayStatus());
        remotePayResult.setOutRespSeqNo(response.getChannelRespSeqNo());
        remotePayResult.setPayUrl(response.getPayUrl());

        return remotePayResult;
    }
}
