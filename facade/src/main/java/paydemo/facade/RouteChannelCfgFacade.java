package paydemo.facade;

import paydemo.facade.model.ChannelCfgRequestDTO;
import paydemo.facade.model.ChannelDetailCfgRequestDTO;
import paydemo.util.Response;


/**
 * @auther YDXiaa
 * <p>
 * 路由配置服务(对第三方支付通道配置).
 */
public interface RouteChannelCfgFacade {

    /**
     * 添加通道配置.
     *
     * @param requestDTO 通道配置对象.
     * @return void.
     */
    Response<Void> addRouteChannelCfg(ChannelCfgRequestDTO requestDTO);

    /**
     * 添加通道详情配置.
     *
     * @param requestDTO 请求对象.
     * @return void.
     */
    Response<Void> addRouteChannelDetailCfg(ChannelDetailCfgRequestDTO requestDTO);

    /**
     * 修改通道详情配置.
     *
     * @param requestDTO 请求对象.
     * @return void.
     */
    Response<Void> modifyRouteChannelDetailCfg(ChannelDetailCfgRequestDTO requestDTO);

}
