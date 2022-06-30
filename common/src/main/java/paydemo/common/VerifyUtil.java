package paydemo.common;

import com.google.common.base.Strings;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;

import java.util.Arrays;
import java.util.List;

/**
 * @auther YDXiaa
 * <p>
 * 验证工具.
 */
public class VerifyUtil {


    /**
     * 验证数据库操作结果.
     *
     * @param result 数据操作数据数量.
     */
    public static void verifySqlResult(int result) {

        if (1 != result) {
            PayException.throwBizFailException(ResponseCodeEnum.UNEXPECTED_UPDATE_CNT);
        }
    }

    /**
     * 验证字段必填.
     *
     * @param key key.
     */
    public static void verifyRequiredField(String... key) {

        List<String> verifyStrs = Arrays.asList(key);
        verifyStrs.forEach(str -> {
            if (Strings.isNullOrEmpty(str)) {
                PayException.throwBizFailException(ResponseCodeEnum.PARAM_VERIFY_FAIL);
            }
        });
    }

}
