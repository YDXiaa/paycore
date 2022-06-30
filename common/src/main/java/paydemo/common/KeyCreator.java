package paydemo.common;

import com.google.common.base.Joiner;

/**
 * @auther YDXiaa
 * <p>
 * redis key创建.
 */
public final class KeyCreator {

    /**
     * 生成Redis Key.
     *
     * @param key key.
     * @return key.
     */
    public static String createRedisKey(final String... key) {

        return Joiner.on("_").join(key);
    }
}
