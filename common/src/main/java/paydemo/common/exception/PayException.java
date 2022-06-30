package paydemo.common.exception;

import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 自定义全局异常.
 */
@Getter
public final class PayException extends RuntimeException {

    private final ResponseCodeEnum responseCode;

    public PayException(ResponseCodeEnum responseCode) {
        super(String.format("PayCore Occured Error,RespCode:%s,RespDesc:%s", responseCode.getRespCode(), responseCode.getRespDesc()));
        this.responseCode = responseCode;
    }

    /**
     * 系统处理异常上抛.
     */
    public static void throwSysErrorException() {
        throwBizFailException(ResponseCodeEnum.SYS_ERROR);
    }

    /**
     * 系统业务异常上抛.
     *
     * @param responseCode 响应码.
     */
    public static void throwBizFailException(final ResponseCodeEnum responseCode) {
        throw new PayException(responseCode);
    }

}
