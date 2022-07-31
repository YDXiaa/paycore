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

        // 未配置,标识是退款场景、查询场景非支付场景或者支付未配置当成渠道无限制.
        if (channelDetailInfoBO.getMinPayAmt() == -1L && channelDetailInfoBO.getMaxPayAmt() == -1L) {
            return true;
        }


        // 最小默认1分最大99999999.
        return payBaseInfoBO.getPayAmt() >= channelDetailInfoBO.getMinPayAmt() &&
                payBaseInfoBO.getPayAmt() <= channelDetailInfoBO.getMaxPayAmt();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
