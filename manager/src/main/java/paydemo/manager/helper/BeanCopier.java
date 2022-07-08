package paydemo.manager.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther YDXiaa
 * <p>
 * 对象拷贝器.
 */
@Slf4j
public final class BeanCopier {

    /**
     * instance.
     */
    public static final BeanCopier beanCopier = new BeanCopier();

    /**
     * getInstance.
     *
     * @return instance.
     */
    public static BeanCopier getInstance() {
        return beanCopier;
    }

    /**
     * 对象拷贝.
     *
     * @param sourceObj 源对象.
     * @param targetObj 目标对象.
     */
    public BeanCopier copy(final Object sourceObj, final Object targetObj) {
        BeanUtils.copyProperties(sourceObj, targetObj);
        return beanCopier;
    }


    /**
     * 对象拷贝.
     *
     * @param sourceObj   源对象.
     * @param targetClazz clazz.
     * @param <T>         对象类型.
     * @return 拷贝完成对象.
     */
    public static <T> T objCopy(final Object sourceObj, Class<T> targetClazz) {

        try {
            T instance = targetClazz.newInstance();
            BeanUtils.copyProperties(sourceObj, instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PayException(ResponseCodeEnum.SYS_ERROR);
        }
    }

    /**
     * 对象拷贝.
     *
     * @param sourceObj 源对象.
     * @param targetObj 目标对象.
     */
    public static void objCopy(final Object sourceObj, final Object targetObj) {
        BeanUtils.copyProperties(sourceObj, targetObj);
    }

    /**
     * 对象列表拷贝.
     *
     * @param sourceObjList 对象List.
     * @param targetClazz   目标对象clazz.
     * @param <T>           目标对象类型.
     * @return 拷贝完成集合对象.
     */
    public static <T> List<T> objListCopy(final List<?> sourceObjList, Class<T> targetClazz) {
        return sourceObjList.stream().map(sourceObj -> objCopy(sourceObj, targetClazz)).collect(Collectors.toList());
    }

}
