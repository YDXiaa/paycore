package paydemo.biz.route.filter;

import org.springframework.stereotype.Component;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

import java.util.Random;

/**
 * @auther YDXiaa
 * <p>
 * 渠道强制权重路由顺序排序.
 */
@Component
public class ChannelForceWeightRouteFilter implements RouteFilter {

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        // 权重 >= 100那么必须走该通道，如果权重相同手续费优先.
        if (channelDetailInfoBO.getChannelWeight() >= 100) {
            // 给一个大值.
            channelDetailInfoBO.setChannelDetailScore(99999999L);
        } else if (channelDetailInfoBO.getChannelWeight() > 0) {
            // 按照比例,例如20%走通道，因为手续费成本问题,会一直走手续费较低的通道，所以此处忽略手续费成本.
            int randomNumber = new Random().nextInt(100);
            if (randomNumber < channelDetailInfoBO.getChannelWeight()) {
                // 给一个大值.
                channelDetailInfoBO.setChannelDetailScore(99999999999L);
            } else {
                // 给一个最小值包装不会第一个被选择到.
                channelDetailInfoBO.setChannelDetailScore(-99999999999L);
            }
        } else {
            // 如果为0或者负数,此处不参与过滤.
            return false;
        }

        return false;
    }

    @Override
    public int getOrder() {
        return 99999;
    }
}
