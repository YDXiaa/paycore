package paydemo.biz.route.filter;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paydemo.dao.dbmodel.ChannelDetailInfoDO;
import paydemo.manager.db.ChannelDetailInfoManager;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

/**
 * @auther YDXiaa
 * <p>
 * 退款/查询业务路由：与支付时是同一个.
 */
@Slf4j
@Component
public class RefundRouteFilter implements RouteFilter {

    @Autowired
    private ChannelDetailInfoManager channelDetailInfoManager;

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        if (!Strings.isNullOrEmpty(payBaseInfoBO.getPayChannelDetailNo())) {
            ChannelDetailInfoDO channelDetailInfoDO = channelDetailInfoManager.queryChannelDetailInfoByDetailNo(
                    payBaseInfoBO.getPayChannelDetailNo());

            log.info("原通道:{},当前过滤通道:{}",channelDetailInfoDO.getChannelNo(),channelDetailInfoBO.getChannelNo());
            // 如果channelNo不相同，过滤该渠道.
            return channelDetailInfoBO.getChannelNo().equals(channelDetailInfoDO.getChannelNo());
        }

        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
