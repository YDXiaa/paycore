package paydemo.biz.route.filter;

import org.springframework.stereotype.Component;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

/**
 * @auther YDXiaa
 * <p>
 * 支付金额过滤对通道金额控制.
 */
@Component
public class PayAmtRouteFilter implements RouteFilter {

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {
        // 最小默认1分最大99999999.
        return payBaseInfoBO.getPayAmt() >= channelDetailInfoBO.getMinPayAmt() &&
                payBaseInfoBO.getPayAmt() <= channelDetailInfoBO.getMaxPayAmt();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
