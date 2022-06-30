package paydemo.dao.mapper;

import paydemo.common.DateUtil;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 动态表名辅助类 mybatisPlus DynamicTableNameInnerInterceptor demo提供 .
 */
public class TableNameHelper {

    /**
     * 请求参数存取.
     */
    private static final ThreadLocal<String> REQUEST_DATA = new ThreadLocal<>();

    /**
     * 设置分表日期.
     *
     * @param shardingDate 分表日期.
     */
    public static void setShardingMark(Date shardingDate) {

        String shardingDateStr = DateUtil.parseDateWith8(shardingDate);

        REQUEST_DATA.set(shardingDateStr);
    }

    /**
     * 通过支付流水号设置分表标记.
     *
     * @param paySeqNo 支付流水号.
     */
    public static void setShardingMark(String paySeqNo) {

        String shardingDateStr = paySeqNo.substring(0, 8);

        REQUEST_DATA.set(shardingDateStr);
    }

    /**
     * 获取分表日期.
     *
     * @return 分表日期.
     */
    public static String getShardingMark() {
        return REQUEST_DATA.get();
    }


}
