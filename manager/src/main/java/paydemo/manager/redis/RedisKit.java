package paydemo.manager.redis;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import paydemo.common.VerifyUtil;

import java.util.concurrent.TimeUnit;

/**
 * @auther YDXiaa
 * <p>
 * redisKit 提供锁、自增流水号生成、缓存处理功能.
 */
@Component
public class RedisKit {


    /**
     * redisTemplate.
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //----------------缓存相关操作--------------------------

    /**
     * 写Redis.
     *
     * @param key     redisKey.
     * @param value   redisValue 要求value为JsonString.
     * @param timeOut timeout SECONDS.
     */
    public void saveCache(String key, String value, Long timeOut) {

        VerifyUtil.verifyRequiredField(key, value);

        redisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
    }

    /**
     * 读Redis.
     *
     * @param key redisKey.
     */
    public String findCache(String key) {

        VerifyUtil.verifyRequiredField(key);

        return redisTemplate.opsForValue().get(key);
    }

    // --------------自增流水号----------------

    /**
     * 自增流水号.
     *
     * @param key Key.
     * @return 自增流水号.
     */
    public String incrSeq(String key) {

        VerifyUtil.verifyRequiredField(key);

        Long num = redisTemplate.opsForValue().increment(key);

        return String.valueOf(num);
    }


    // ------------------lock功能-------------------------

    /**
     * redis锁(默认五分钟).
     *
     * @param key   key.
     * @param value value.
     * @return 加锁结果.
     */
    public Boolean defaultLock(String key, String value) {

        return lock(key, value, 5 * 60L);
    }

    /**
     * redis锁.
     *
     * @param key     key.
     * @param value   value.
     * @param timeOut 超时时间.
     * @return 加锁结果.
     */
    public Boolean lock(String key, String value, Long timeOut) {

        return lock(key, value, timeOut, TimeUnit.SECONDS);
    }

    /**
     * redis锁 只有当值不存在才设置Key ExpireTime.
     *
     * @param key      key.
     * @param value    value.
     * @param timeOut  超时时间.
     * @param timeUnit 时间单位.
     * @return 加锁结果.
     */
    public Boolean lock(String key, String value, Long timeOut, TimeUnit timeUnit) {

        VerifyUtil.verifyRequiredField(key, value);

        return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, timeUnit);
    }

    /**
     * redis解锁.
     *
     * @param key   key.
     * @param value value.
     * @return 解锁结果.
     */
    public Boolean unLock(String key, String value) {

        VerifyUtil.verifyRequiredField(key, value);

        // 先比较redisValue.
        String redisValue = redisTemplate.opsForValue().get(key);
        if (Strings.isNullOrEmpty(redisValue)) {
            return true;
        }

        // 是同一个值才删除.
        if (redisValue.equals(value)) {
            redisTemplate.delete(key);
            return true;
        }

        // 解锁失败.
        return false;
    }

}
