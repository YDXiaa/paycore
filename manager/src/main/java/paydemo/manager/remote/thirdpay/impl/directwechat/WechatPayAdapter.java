package paydemo.manager.remote.thirdpay.impl.directwechat;

import org.springframework.stereotype.Service;
import paydemo.common.PayStatusEnum;
import paydemo.common.RemotePayResult;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.manager.remote.thirdpay.BaseRemoteServiceAdapter;
import paydemo.manager.remote.thirdpay.DubboServiceRegistry;
import paydemo.manager.remote.thirdpay.impl.mock.MockDirectWechatPayRequestDTO;
import paydemo.manager.remote.thirdpay.impl.mock.RemotePayResponse;

import java.util.UUID;

/**
 * @auther YDXiaa
 */
@Service
public class WechatPayAdapter extends DubboServiceRegistry implements BaseRemoteServiceAdapter<MockDirectWechatPayRequestDTO, RemotePayResponse> {


    @Override
    public MockDirectWechatPayRequestDTO createServiceReq(RemoteRequestModel model) {
        MockDirectWechatPayRequestDTO mockDirectWechatPayRequestDTO = new MockDirectWechatPayRequestDTO();
        mockDirectWechatPayRequestDTO.setPayAmt(model.getPayAmt());
        return mockDirectWechatPayRequestDTO;
    }

    @Override
    public RemotePayResponse doService(MockDirectWechatPayRequestDTO request) {
//        return getDubboService(MockWechatPayServiceFacade.class).pay(request);
        return new RemotePayResponse();
    }

    @Override
    public RemotePayResult createServiceResp(RemoteRequestModel model, RemotePayResponse reult) {
        RemotePayResult remotePayResult = new RemotePayResult();
        remotePayResult.setPayStatus(PayStatusEnum.SUCCESS.getStatusCode());
        remotePayResult.setRealRtn(true);
        remotePayResult.setOutRespSeqNo(UUID.randomUUID().toString());
        return remotePayResult;
    }
}
