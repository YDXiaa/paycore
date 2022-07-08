package paydemo.biz.route.filter;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

/**
 * @auther YDXiaa
 * <p>
 * 重新支付路由过滤器，如果渠道明确失败，可以送其他通道进行尝试.
 */
@Component
public class RePayRouteFilter implements RouteFilter {

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        // 如果传入上次支付失败路由渠道编号,那么该渠道剔除.
        if (!Strings.isNullOrEmpty(payBaseInfoBO.getPayFailChannelDetailNo())) {
            return payBaseInfoBO.getPayFailChannelDetailNo().equals(channelDetailInfoBO.getChannelDetailNo());
        }

        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
