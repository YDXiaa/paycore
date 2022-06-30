package paydemo.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 日期工具.
 */
public final class DateUtil {

    /**
     * 默认时间样式.
     */
    public static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyyMMddHHmmss 格式.
     */
    public static final String TIME_PATTERN_14 = "yyyyMMddHHmmss";

    /**
     * yyyyMMdd 格式.
     */
    public static final String TIME_PATTERN_8 = "yyyyMMdd";


    /**
     * 解析日期.
     *
     * @param date 日期.
     * @return 日期格式.
     */
    public static String parseDateWith14(final Date date) {
        return parseDate(date, TIME_PATTERN_14);
    }

    /**
     * 解析日期 yyyyMMdd.
     *
     * @param date 日期.
     * @return 日期格式.
     */
    public static String parseDateWith8(final Date date) {
        return parseDate(date, TIME_PATTERN_8);
    }

    /**
     * 解析日期.
     *
     * @param date    日期.
     * @param pattern 样式.
     * @return 日期格式.
     */
    public static String parseDate(final Date date, final String pattern) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return formatLocalDateTime(localDateTime, pattern);
    }

    private static String formatLocalDateTime(final LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 指定日期后指定秒数后时间.
     *
     * @param date         指定日期.
     * @param afterSeconds 往后指定秒数.
     * @return 指定秒数后的日期对象.
     */
    public static Date after(Date date, Long afterSeconds) {
        return date;
    }
}
