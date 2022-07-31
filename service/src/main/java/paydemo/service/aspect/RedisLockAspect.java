package paydemo.service.aspect;

import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.redis.RedisKit;
import paydemo.util.KeyCreator;
import paydemo.util.ReflectUtil;
import paydemo.util.Response;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * @auther YDXiaa
 * <p>
 * redisLock切面处理.
 */
@Aspect
@Order(1)
@Component
public class RedisLockAspect {

    @Autowired
    private RedisKit redisKit;

    /**
     * 切点 @RequiredLock 注解.
     */
    @Pointcut("@annotation(paydemo.service.aspect.RequiredLockControl)")
    public void lockPointCut() {
    }

    @Around("lockPointCut()")
    public Object lock(ProceedingJoinPoint joinPoint) {

        // 获取调用方法.
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiredLockControl lock = method.getAnnotation(RequiredLockControl.class);

        // redisKey.
        String lockRedisKeyPrefix = lock.keyPrefix().getRedisKeyCode();

        // lockName.
        String[] lockFieldNames = lock.fields();

        // lockTime.
        long lockTime = lock.lockTime();

        // lockField.
        List<String> lockFieldValues = Lists.newArrayList(lockRedisKeyPrefix);

        // requestObj.
        Object request = joinPoint.getArgs()[0];

        for (String lockFieldName : lockFieldNames) {
            String fieldValue = ReflectUtil.readFiledValue(request, lockFieldName);
            lockFieldValues.add(fieldValue);
        }

        // createRedisKey.
        String redisKey = KeyCreator.createRedisKey(lockFieldValues.toArray(new String[0]));

        // 加锁结果标识.
        boolean lockSuccessFlag;
        // 生成随机数作为value.
        String randomId = UUID.randomUUID().toString();
        try {

            // 加锁.
            lockSuccessFlag = redisKit.lock(redisKey, randomId, lockTime);
            if (!lockSuccessFlag) {
                return Response.createFailResponse(ResponseCodeEnum.REPEAT_ORDER.getRespCode(), ResponseCodeEnum.REPEAT_ORDER.getRespDesc());
            }
            // 业务处理.
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            return Response.createFailResponse(ResponseCodeEnum.SYS_ERROR.getRespCode(), ResponseCodeEnum.SYS_ERROR.getRespDesc());
        } finally {
            redisKit.unLock(redisKey, randomId);
        }
    }

}
