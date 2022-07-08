package paydemo.biz.route.filter;

import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

/**
 * @auther YDXiaa
 * <p>
 * 路由过滤器.
 */
public interface RouteFilter {

    /**
     * 过滤通道.
     *
     * @param channelDetailInfoBO 通道详情.
     * @param payBaseInfoBO       支付基础信息.
     * @return 过滤结果.
     */
    boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO);

    /**
     * 执行顺序.
     *
     * @return filter执行顺序.
     */
    int getOrder();

}
