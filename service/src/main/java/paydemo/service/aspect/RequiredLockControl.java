package paydemo.service.aspect;

import paydemo.common.RedisKeyEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识业务需要并发控制,使用Spring Aspect切面统一控制，简化代码.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RequiredLockControl {

    /**
     * redis锁名称 前缀.
     */
    RedisKeyEnum keyPrefix() default RedisKeyEnum.PAYCORE_PAY;

    /**
     * 参数字段名称，从字段中取值作为锁.
     */
    String[] fields() default "";

    /**
     * 加锁时间.
     *
     * @return 加锁时间
     */
    long lockTime() default 300L;

}
