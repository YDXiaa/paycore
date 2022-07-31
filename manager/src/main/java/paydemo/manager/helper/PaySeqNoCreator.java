package paydemo.manager.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paydemo.common.*;
import paydemo.manager.redis.RedisKit;
import paydemo.util.StringsExt;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 支付号生成器.
 */
@Component
public class PaySeqNoCreator {

    @Autowired
    private RedisKit redisKit;

    /**
     * 创建交易流水号 36.
     *
     * @param requestDate 请求日期.
     * @param bizLineName 业务线.
     * @param payTypeCode 支付类型编码.
     * @return 支付号.
     */
    public String createPaySeqNo(Date requestDate, String bizLineName, String payTypeCode, RedisKeyEnum redisKeyEnum) {

        // yyyyMMddHHmmss(14位)
        String payReqDate = DateUtil.parseDateWith14(requestDate);

        // 业务线类型(4位)(todo 系统中配置).
        String bizCode = BizLineEnum.getBizCode(bizLineName);

        // 支付类型服务编码(2位).
        String payTypeServiceCode = PayTypeEnum.getTransCode(payTypeCode);

        // 16位自增流水号号.
        String seq = StringsExt.padZeroLeft(redisKit.incrSeq(redisKeyEnum.getRedisKeyCode()), 16);

        return StringsExt.joinStrWithEmpty(payReqDate, bizCode, payTypeServiceCode, seq);
    }

    /**
     * 创建渠道请求序列号.
     *
     * @param requestDate 请求日期.
     * @param seqLength   序列号长度.
     * @return 渠道请求序列号.
     */
    public String createChannelSeqNo(Date requestDate, Long seqLength) {

        // yyyyMMddHHmmss(14位)
        String payReqDate = DateUtil.parseDateWith14(requestDate);

        // 16位自增流水号号.
        String seq = StringsExt.padZeroLeft(redisKit.incrSeq(RedisKeyEnum.PAYCORE_CHANNEL_SEQ_NO.getRedisKeyCode()),
                seqLength.intValue() - 14);

        return StringsExt.joinStrWithEmpty(payReqDate, seq);
    }
}
