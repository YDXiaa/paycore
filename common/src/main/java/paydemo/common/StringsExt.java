package paydemo.common;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * @auther YDXiaa
 * <p>
 * guava String增强工具包.
 */
public final class StringsExt {


    /**
     * Strings增强 用于左侧固定长度补0.
     *
     * @param targetStr 目标字符串.
     * @param fixLength 固定长度.
     * @return 最终字符串.
     */
    public static String padZeroLeft(String targetStr, int fixLength) {

        if (Strings.isNullOrEmpty(targetStr)) {
            return null;
        }

        // 超过长度直接截取.
        if (targetStr.length() > fixLength) {
            return targetStr.substring(targetStr.length() - fixLength - 1, fixLength - 1);
        }

        // 左侧补0.
        return Strings.padStart(targetStr, fixLength, '0');
    }

    /**
     * 拼接字符串默认默认直接拼接.
     *
     * @param values values.
     * @return targetStr.
     */
    public static String joinStrWithEmpty(String... values) {

        return joinStr("", values);
    }

    /**
     * 指定字符串拼接字符串.
     *
     * @param seq    分隔符.
     * @param values 值集合.
     * @return 目标字符串.
     */
    public static String joinStr(String seq, String... values) {
        return Joiner.on(seq).join(values);
    }

}
