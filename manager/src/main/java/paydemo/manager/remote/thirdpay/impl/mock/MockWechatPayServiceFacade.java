package paydemo.manager.remote.thirdpay.impl.mock;

/**
 * @auther YDXiaa
 * <p>
 * MockWeChatPayService.
 */
public interface MockWechatPayServiceFacade {

    /**
     * 支付.
     *
     * @param requestDTO requestDto.
     * @return resp.
     */
    RemotePayResponse pay(MockDirectWechatPayRequestDTO requestDTO);

}
