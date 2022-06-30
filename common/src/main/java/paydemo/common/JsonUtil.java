package paydemo.common;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;

/**
 * @auther YDXiaa
 * <p>
 * Json工具.
 */
public final class JsonUtil {

    /**
     * 单例.
     */
    private static final Gson INSTANCE = new Gson();

    /**
     * 转换为 json 字符串.
     *
     * @param targetObj 转换目标对象.
     * @return json 字符串.
     */
    public static String toJsonStr(Object targetObj) {

        Preconditions.checkArgument(null != targetObj, "targetObj must be not NULL");

        return INSTANCE.toJson(targetObj);
    }

    /**
     * json字符串转化为对象.
     *
     * @param jsonStr jsonStr 字符串.
     * @param clazz   class类型.
     * @param <T>     转换class类型.
     * @return 对象.
     */
    public static <T> T parseJsonStr2Obj(String jsonStr, Class<T> clazz) {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(jsonStr), "jsonStr must be not NULL");

        return INSTANCE.fromJson(jsonStr, clazz);
    }
}
