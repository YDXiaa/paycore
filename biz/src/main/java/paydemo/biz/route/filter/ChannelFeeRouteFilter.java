package paydemo.biz.route.filter;

import org.springframework.stereotype.Component;
import paydemo.common.BillingTypeEnum;
import paydemo.common.FeeCalModeEnum;
import paydemo.common.SortPriorityEnum;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;
import paydemo.util.FlagsEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @auther YDXiaa
 * <p>
 * 渠道手续费计算.
 */
@Component
public class ChannelFeeRouteFilter implements RouteFilter {

    @Override
    public boolean filter(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        // 计算手续费.
        calChannelFeeAmt(channelDetailInfoBO, payBaseInfoBO);

        // 计算优先级 手续费越高优先级越低 全部计算成手续费换算成比率计算.
        channelDetailInfoBO.setChannelDetailScore(channelDetailInfoBO.getChannelDetailScore() -
                SortPriorityEnum.FEE_COST.getSortPriorityCode() * channelDetailInfoBO.getFeeAmtProportion());

        return false;
    }

    /**
     * 计算渠道手续费.
     *
     * @param channelDetailInfoBO 渠道详情.
     * @param payBaseInfoBO       支付基础信息.
     */
    private void calChannelFeeAmt(ChannelDetailInfoBO channelDetailInfoBO, PayBaseInfoBO payBaseInfoBO) {

        BigDecimal feeAmt = BigDecimal.ZERO;

        // 不收费.
        if (channelDetailInfoBO.getFeeFlag().equals(FlagsEnum.FALSE.getFlagCode()) ||
                channelDetailInfoBO.getFeeBeginAmt() > payBaseInfoBO.getPayAmt()) {
            channelDetailInfoBO.setChannelFeeAmt(feeAmt.longValue());
            channelDetailInfoBO.setFeeAmtProportion(0L);
            return;
        }

        // 计算手续费.
        if (FeeCalModeEnum.SINGLE.getFeeCalModeCode().equals(channelDetailInfoBO.getFeeCalMode())) {
            // 固定类型计费.
            if (BillingTypeEnum.FIXED.getBillingTypeCode().equals(channelDetailInfoBO.getBillingType())) {
                feeAmt = channelDetailInfoBO.getBillingValue();
            } else if (BillingTypeEnum.RATE.getBillingTypeCode().equals(channelDetailInfoBO.getBillingType())) {
                // 比率计费.
                feeAmt = channelDetailInfoBO.getBillingValue().multiply(BigDecimal.valueOf(payBaseInfoBO.getPayAmt()));
            }
        } else {
            PayException.throwBizFailException(ResponseCodeEnum.FEE_CAL_MODE_ERROR);
        }

        // 最小计费.
        if (feeAmt.compareTo(BigDecimal.valueOf(channelDetailInfoBO.getFeeMinAmt())) < 0) {
            feeAmt = BigDecimal.valueOf(channelDetailInfoBO.getFeeMinAmt());
        }

        // 手续费占比
        double feeAmtProportion = feeAmt.divide(new BigDecimal(payBaseInfoBO.getPayAmt()),
                2, RoundingMode.HALF_UP).doubleValue();

        // 手续费.
        channelDetailInfoBO.setChannelFeeAmt(feeAmt.longValue());
        // 手续费占比.
        channelDetailInfoBO.setFeeAmtProportion(feeAmtProportion);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
