package paydemo.service;

import org.springframework.stereotype.Service;
import paydemo.facade.RouteChannelCfgFacade;
import paydemo.facade.model.ChannelCfgRequestDTO;
import paydemo.facade.model.ChannelDetailCfgRequestDTO;
import paydemo.facade.model.Response;

/**
 * @auther YDXiaa
 */
@Service
public class RouteChannelCfgServiceImpl implements RouteChannelCfgFacade {

    @Override
    public Response<Void> addRouteChannelCfg(ChannelCfgRequestDTO requestDTO) {
        return null;
    }

    @Override
    public Response<Void> addRouteChannelDetailCfg(ChannelDetailCfgRequestDTO requestDTO) {
        return null;
    }

    @Override
    public Response<Void> modifyRouteChannelDetailCfg(ChannelDetailCfgRequestDTO requestDTO) {
        return null;
    }
}
