package paydemo.biz.route.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paydemo.manager.helper.ChannelDetailAccumulateHelper;
import paydemo.common.SortPriorityEnum;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.ChannelDetailSummaryBO;
import paydemo.manager.model.PayBaseInfoBO;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @auther YDXiaa
 * <p>
 * 成功率路由排序过滤器.
 */
@Component
public class SuccessRateRouteFilter implements RouteFilter {

    @Autowired
    private ChannelDetailAccumulateHelper accuBiz;

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        // todo SuccessRate通过Redis累加三分钟或者五分钟内的交易成功率(业务失败(微信支付宝余额不足、放弃支付)不参与统计) 需要维护错误码.
        ChannelDetailSummaryBO channelDetailSummay = accuBiz.getChannelDetailSummay(channelDetailInfoBO.getChannelDetailNo());

        // 计算成功率.
        Double successRate = calSuccessRate(channelDetailSummay);

        // 设置成功率权重.
        channelDetailInfoBO.setChannelDetailScore(channelDetailInfoBO.getChannelDetailScore() +
                SortPriorityEnum.SUCCESS_RATE.getSortPriorityCode() * successRate);

        return true;
    }

    /**
     * 计算成功率.
     *
     * @param channelDetailSummay summary.
     * @return 成功率.
     */
    private Double calSuccessRate(ChannelDetailSummaryBO channelDetailSummay) {

        // 总交易笔数.
        long totalCnt = channelDetailSummay.getSuccessCnt() + channelDetailSummay.getPayingCnt() + channelDetailSummay.getFailCnt();

        if (totalCnt == 0L) {
            return 0.0D;
        }

        return BigDecimal.valueOf(channelDetailSummay.getSuccessCnt())
                .divide(BigDecimal.valueOf(totalCnt), 2, RoundingMode.HALF_UP).doubleValue();

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
