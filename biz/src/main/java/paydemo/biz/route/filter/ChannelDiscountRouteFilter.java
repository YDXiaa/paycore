package paydemo.biz.route.filter;

import org.springframework.stereotype.Component;
import paydemo.common.FlagsEnum;
import paydemo.common.SortPriorityEnum;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

/**
 * @auther YDXiaa
 * <p>
 * 渠道用户优惠信息.
 */
@Component
public class ChannelDiscountRouteFilter implements RouteFilter {

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        // 渠道营销折扣标识.
        if(FlagsEnum.TRUE.getFlagCode().equals(channelDetailInfoBO.getDiscountFlag())){
            channelDetailInfoBO.setChannelDetailScore(channelDetailInfoBO.getChannelDetailScore() +
                    SortPriorityEnum.PAY_DISCOUNT.getSortPriorityCode() * 0.5);
        }

        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
