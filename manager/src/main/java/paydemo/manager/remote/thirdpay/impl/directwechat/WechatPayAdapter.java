package paydemo.manager.remote.thirdpay.impl.directwechat;

import org.springframework.stereotype.Service;
import paydemo.common.PayStatusEnum;
import paydemo.common.RemotePayResult;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.manager.remote.thirdpay.BaseRemoteServiceAdapter;
import paydemo.manager.remote.thirdpay.DubboServiceRegistry;
import paydemo.paygateway.facade.model.GatewayPayResponse;
import paydemo.paygateway.facade.model.GwResponse;
import paydemo.paygateway.facade.model.pay.DirectWeChatPayRequestDTO;
import paydemo.paygateway.facade.pay.DirectWeChatPayFacade;

/**
 * @auther YDXiaa
 * <p>
 * 微信支付.
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
            GwResponse<GatewayPayResponse> gwResponse = getDubboService(DirectWeChatPayFacade.class).pay(request);
            return gwResponse.getRespData();
        } catch (Throwable throwable) {
            GatewayPayResponse payResponse = new GatewayPayResponse();
            if (invokeTimeOut(throwable)) {
                payResponse.setPayStatus(PayStatusEnum.PAYING.getStatusCode());
                payResponse.setResultCode(ResponseCodeEnum.REQUEST_CHANNEL_TIMEOUT.getRespCode());
                payResponse.setResultDesc(ResponseCodeEnum.REQUEST_CHANNEL_TIMEOUT.getRespDesc());
            } else {
                payResponse.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
                payResponse.setResultCode(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespCode());
                payResponse.setResultDesc(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespDesc());
            }
            return payResponse;
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
