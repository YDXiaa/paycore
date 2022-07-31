package paydemo.manager.remote.thirdpay.impl.directwechat;

import org.springframework.stereotype.Service;
import paydemo.common.RemotePayResult;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.manager.remote.thirdpay.BaseRemoteServiceAdapter;
import paydemo.manager.remote.thirdpay.DubboServiceRegistry;
import paydemo.paygateway.facade.model.GatewayPayResponse;
import paydemo.paygateway.facade.model.GwResponse;
import paydemo.paygateway.facade.model.pay.DirectWeChatPayQueryRequestDTO;
import paydemo.paygateway.facade.model.pay.DirectWeChatPayRequestDTO;
import paydemo.paygateway.facade.pay.DirectWeChatPayFacade;
import paydemo.util.PayStatusEnum;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 微信支付.
 */
@Service
public class WechatPayQueryAdapter extends DubboServiceRegistry implements BaseRemoteServiceAdapter<DirectWeChatPayQueryRequestDTO,
        GatewayPayResponse> {

    @Override
    public DirectWeChatPayQueryRequestDTO createServiceReq(RemoteRequestModel model) {

        DirectWeChatPayQueryRequestDTO requestDTO = new DirectWeChatPayQueryRequestDTO();
        requestDTO.setChannelRequestSeqNo(model.getOutRequestSeqNo());
        return requestDTO;
    }

    @Override
    public GatewayPayResponse doService(DirectWeChatPayQueryRequestDTO request) {

        try {
            Response<GatewayPayResponse> gwResponse = getDubboService(DirectWeChatPayFacade.class).payQuery(request);
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
