package paydemo.manager.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.KeyCreator;
import paydemo.common.PayStatusEnum;
import paydemo.common.RedisKeyEnum;
import paydemo.manager.model.ChannelDetailSummaryBO;
import paydemo.manager.redis.RedisKit;

import java.util.List;
import java.util.Objects;

/**
 * @auther YDXiaa
 * <p>
 * 成功率统计,使用Redis hash结构存储.
 */
@Slf4j
@Service
public class ChannelDetailAccumulateHelper {

    @Autowired
    private RedisKit redisKit;

    /**
     * 渠道成功率交易统计.
     *
     * @param payStatus       支付状态.
     * @param channelDetailNo 渠道详情编码.
     */
    public void accumulateChannelDetail(String payStatus, String channelDetailNo) {

        // redisKey.
        String succRateKey = KeyCreator.createRedisKey(RedisKeyEnum.PAYCORE_SUCCESS_RATE.getRedisKeyCode(), channelDetailNo);

        if (PayStatusEnum.SUCCESS.getStatusCode().equals(payStatus)) {
            redisKit.saveCache4Hash(succRateKey, RedisKeyEnum.SUCCESS_CNT.getRedisKeyCode(), 1L, 600L);
        } else if (PayStatusEnum.FAIL.getStatusCode().equals(payStatus)) {
            redisKit.saveCache4Hash(succRateKey, RedisKeyEnum.FAIL_CNT.getRedisKeyCode(), 1L, 600L);
        } else {
            redisKit.saveCache4Hash(succRateKey, RedisKeyEnum.PAYING_CNT.getRedisKeyCode(), 1L, 600L);
        }
    }

    /**
     * 获取渠道交易汇总.
     *
     * @param channelDetailNo 渠道详情编号.
     * @return 交易汇总.
     */
    public ChannelDetailSummaryBO getChannelDetailSummay(String channelDetailNo) {

        // redisKey.
        String succRateKey = KeyCreator.createRedisKey(RedisKeyEnum.PAYCORE_SUCCESS_RATE.getRedisKeyCode(), channelDetailNo);

        List<Object> channelDetailSummary = redisKit.findCache4Hash(succRateKey, RedisKeyEnum.SUCCESS_CNT.getRedisKeyCode(),
                RedisKeyEnum.FAIL_CNT.getRedisKeyCode(), RedisKeyEnum.PAYING_CNT.getRedisKeyCode());

        Object successCnt = channelDetailSummary.get(0);
        Object failCnt = channelDetailSummary.get(1);
        Object payingCnt = channelDetailSummary.get(2);

        ChannelDetailSummaryBO summaryBO = new ChannelDetailSummaryBO();

        summaryBO.setSuccessCnt(Objects.isNull(successCnt) ? 0L : (Long) successCnt);
        summaryBO.setFailCnt(Objects.isNull(failCnt) ? 0L : (Long) failCnt);
        summaryBO.setPayingCnt(Objects.isNull(payingCnt) ? 0L : (Long) payingCnt);

        return summaryBO;
    }

}
