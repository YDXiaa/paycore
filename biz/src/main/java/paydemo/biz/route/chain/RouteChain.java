package paydemo.biz.route.chain;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.biz.route.filter.*;
import paydemo.common.BizTypeEnum;
import paydemo.common.RouteFilterTypeEnum;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.db.ChannelDetailInfoManager;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auther YDXiaa
 * <p>
 * 路由过滤链.
 */
@Service
public class RouteChain {

    /**
     * 渠道服务不可用,维护时间.
     */
    @Autowired
    private NoServiceRouteFilter noServiceRouteFilter;

    /**
     * 退款路由过滤.
     */
    @Autowired
    private RefundRouteFilter refundRouteFilter;

    /**
     * 交易金额过滤.
     */
    @Autowired
    private PayAmtRouteFilter payAmtRouteFilter;

    /**
     * 强制路由过滤.
     */
    @Autowired
    private ChannelForceWeightRouteFilter channelForceWeightRouteFilter;

    /**
     * 渠道手续费过滤.
     */
    @Autowired
    private ChannelFeeRouteFilter channelFeeRouteFilter;

    /**
     * 渠道成功率.
     */
    @Autowired
    private SuccessRateRouteFilter successRateRouteFilter;

    /**
     * 渠道营销优惠信息路由过滤器.
     */
    @Autowired
    private ChannelDiscountRouteFilter channelDiscountRouteFilter;

    /**
     * 支付类型多选路由过滤器.
     */
    @Autowired
    private PayTypeRouteFilter payTypeRouteFilter;


    @Autowired
    private ChannelDetailInfoManager channelDetailInfoManager;

    /**
     * routeFilterMap.
     */
    private static final Map<RouteFilterTypeEnum, List<RouteFilter>> FILTER_CONTAINER = Maps.newHashMap();

    @PostConstruct
    public void initFilter() {

        // 通用过滤.
        List<RouteFilter> commonRouteFilterList = Lists.newArrayList(noServiceRouteFilter,
                refundRouteFilter,
                payAmtRouteFilter);

        // 路由排序.
        List<RouteFilter> sortRouteFilterList = Lists.newArrayList(successRateRouteFilter,
                channelFeeRouteFilter,
                channelForceWeightRouteFilter,
                channelDiscountRouteFilter,
                payTypeRouteFilter);

        FILTER_CONTAINER.put(RouteFilterTypeEnum.PLAIN, commonRouteFilterList);
        FILTER_CONTAINER.put(RouteFilterTypeEnum.SORT, sortRouteFilterList);
    }


    /**
     * 支付渠道选择.
     *
     * @param payBaseInfoBO 支付基础信息.
     * @return 渠道详情信息.
     */
    public ChannelDetailInfoBO getRouteChannel(PayBaseInfoBO payBaseInfoBO) {

        //  如果AvailablePayTypes为空，使用payTypes.
        if (CollectionUtil.isEmpty(payBaseInfoBO.getAvailablePayTypes())) {
            payBaseInfoBO.setAvailablePayTypes(Lists.newArrayList(payBaseInfoBO.getPayType()));
        }

        if(StrUtil.isBlank(payBaseInfoBO.getBizType())){
            payBaseInfoBO.setBizType(BizTypeEnum.DEFAULT.getBizTypeCode());
        }

        List<ChannelDetailInfoBO> channelDetailInfoBOList = channelDetailInfoManager.queryChannelDetailInfo(payBaseInfoBO.getPayTool(),
                payBaseInfoBO.getPaySubTool(), payBaseInfoBO.getAvailablePayTypes(), payBaseInfoBO.getBizType());

        // 1、普通过滤器
        List<ChannelDetailInfoBO> commonFilteredList = channelDetailInfoBOList.stream().filter(channelDetailInfoBO -> {
            List<RouteFilter> filterList = FILTER_CONTAINER.get(RouteFilterTypeEnum.PLAIN);
            // 所有filter必须返回true.
            return filterList.stream().allMatch(filter -> filter.filter(channelDetailInfoBO, payBaseInfoBO));
        }).collect(Collectors.toList());

        // 2、排序过滤器
        return commonFilteredList.stream().peek(channelDetailInfoBO -> {
            List<RouteFilter> filterList = FILTER_CONTAINER.get(RouteFilterTypeEnum.SORT);
            filterList.forEach(filter -> filter.filter(channelDetailInfoBO, payBaseInfoBO));
        }).max(Comparator.comparingDouble(ChannelDetailInfoBO::getChannelDetailScore)).orElseThrow(() -> new PayException(ResponseCodeEnum.USABLE_CHANNEL_NOT_EXISTS));
    }
}
