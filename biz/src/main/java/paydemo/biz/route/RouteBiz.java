package paydemo.biz.route;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.biz.route.chain.RouteChain;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

/**
 * @auther YDXiaa
 * <p>
 * 三方支付简单路由流程.
 */
@Slf4j
@Service
public class RouteBiz {

    @Autowired
    private RouteChain routeChain;

    /**
     * 路由流程.
     *
     * @param payBaseInfoBO 支付基础信息.
     * @return 路由渠道.
     */
    public ChannelDetailInfoBO route(PayBaseInfoBO payBaseInfoBO) {
        try {
            return routeChain.getRouteChannel(payBaseInfoBO);
        }catch (Throwable throwable){
            log.error("异常信息:{}", Throwables.getStackTraceAsString(throwable));
        }
        return null;
    }

}
