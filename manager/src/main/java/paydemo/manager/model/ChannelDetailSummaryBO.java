package paydemo.manager.model;

import lombok.Data;

/**
 * @auther YDXiaa
 * <p>
 * 渠道详细交易汇总.
 */
@Data
public class ChannelDetailSummaryBO {

    /**
     * 成功交易笔数.
     */
    private Long successCnt = 0L;

    /**
     * 交易失败笔数.
     */
    private Long failCnt = 0L;

    /**
     * 支付中笔数.
     */
    private Long payingCnt = 0L;

}
