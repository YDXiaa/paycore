package paydemo.biz.route.filter;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import paydemo.common.SortPriorityEnum;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

/**
 * @auther YDXiaa
 * <p>
 * 支付类型过滤器,存在多个支付类型场景下实现同一功能.
 */
@Component
public class PayTypeRouteFilter implements RouteFilter {


    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        if (StrUtil.equals(channelDetailInfoBO.getPayType(), payBaseInfoBO.getPayType())) {
            channelDetailInfoBO.setChannelDetailScore(channelDetailInfoBO.getChannelDetailScore() +
                    SortPriorityEnum.PAY_TYPE.getSortPriorityCode());
        }

        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
