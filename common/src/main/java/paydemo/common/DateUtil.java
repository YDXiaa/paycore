package paydemo.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

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
     * 按照指定日期格式解析Date日期.
     *
     * @param date    日期.
     * @param pattern 样式.
     * @return 日期格式.
     */
    public static String parseDate(final Date date, final String pattern) {
        return formatLocalDateTime(createLocalDateTimeOfDate(date), pattern);
    }

    /**
     * localDateTime解析为指定日期格式.
     *
     * @param dateTime dateTime.
     * @param pattern  pattern.
     * @return 指定日期格式显示.
     */
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
        LocalDateTime afterLocalDateTime = createLocalDateTimeOfDate(date).plusSeconds(afterSeconds);
        return Date.from(afterLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 是否在此时间之前.
     *
     * @param date date.
     * @return 比较结果.
     */
    public static boolean isBeforeNow(final Date date) {

        return createLocalDateTimeOfDate(date).isBefore(LocalDateTime.now());
    }


    /**
     * 是否在此时间之后.
     *
     * @param date date.
     * @return 比较结果.
     */
    public static boolean isAfterNow(final Date date) {

        return createLocalDateTimeOfDate(date).isAfter(LocalDateTime.now());
    }

    /**
     * 创建默认LocalDateTime对象.
     *
     * @param date date.
     * @return localDateTime.
     */
    public static LocalDateTime createLocalDateTimeOfDate(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
