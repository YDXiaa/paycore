package paydemo.common;

import java.lang.reflect.Field;

/**
 * @auther YDXiaa
 * <p>
 * 反射工具.
 */
public class ReflectUtil {


    /**
     * 读取指定字段值.
     *
     * @param targetObj 目标对象.
     * @param fieldName 字段值.
     * @return 读取结果(目前都强制指定为String).
     */
    public static String readFiledValue(Object targetObj, String fieldName) {

        try {
            Field field = targetObj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(targetObj);
        } catch (Exception ex) {
            return null;
        }
    }
}
