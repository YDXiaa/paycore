package paydemo.biz.route.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import paydemo.common.DateUtil;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

import java.util.Objects;

/**
 * @auther YDXiaa
 * <p>
 * 渠道维护路由过滤器.
 */
@Slf4j
@Component
public class NoServiceRouteFilter implements RouteFilter {

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        // 渠道未维护.
        if (Objects.isNull(channelDetailInfoBO.getNoServiceBeginTime()) && Objects.isNull(channelDetailInfoBO.getNoServiceEndTime())) {
            log.debug("渠道未配置维护时间,默认通过");
            return true;
        }

        // (now)-----noServiceBeginTime -----------noServiceEndTime -----(now)
        return DateUtil.isAfterNow(channelDetailInfoBO.getNoServiceBeginTime()) && DateUtil.isBeforeNow(channelDetailInfoBO.getNoServiceEndTime());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
