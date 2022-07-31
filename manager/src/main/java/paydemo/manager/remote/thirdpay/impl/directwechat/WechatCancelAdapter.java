package paydemo.manager.remote.thirdpay.impl.directwechat;

import org.springframework.stereotype.Service;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.manager.remote.thirdpay.BaseRemoteServiceAdapter;
import paydemo.manager.remote.thirdpay.DubboServiceRegistry;
import paydemo.paygateway.facade.model.GatewayPayResponse;
import paydemo.paygateway.facade.model.pay.DirectWeChatCancelRequestDTO;
import paydemo.paygateway.facade.pay.DirectWeChatPayFacade;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 微信支付订单撤销.
 */
@Service
public class WechatCancelAdapter extends DubboServiceRegistry implements BaseRemoteServiceAdapter<DirectWeChatCancelRequestDTO,
        GatewayPayResponse> {

    @Override
    public DirectWeChatCancelRequestDTO createServiceReq(RemoteRequestModel model) {

        DirectWeChatCancelRequestDTO requestDTO = new DirectWeChatCancelRequestDTO();
        requestDTO.setChannelRequestSeqNo(model.getOutRequestSeqNo());
        return requestDTO;
    }

    @Override
    public GatewayPayResponse doService(DirectWeChatCancelRequestDTO request) {

        try {
            Response<GatewayPayResponse> gwResponse = getDubboService(DirectWeChatPayFacade.class).cancel(request);
            return gwResponse.getRespData();
        } catch (Throwable throwable) {
            return invokeException(throwable);
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
