package paydemo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    /**
     * 业务处理成功.
     */
    BIZ_SUCCESS("BIZ_SUCCESS", "业务处理成功"),

    /**
     * 系统发生错误.
     */
    SYS_ERROR("SYS_ERROR", "系统处理错误"),

    /**
     * 参数验证失败.
     */
    PARAM_VERIFY_FAIL("PARAM_VERIFY_FAIL", "参数验证失败"),

    /**
     * 重复下单.
     */
    REPEAT_ORDER("REPEAT_ORDER", "RedisLock加锁失败,请不要重复下单"),

    /**
     * 订单已存在.
     */
    ORDER_SYS_EXISTS("ORDER_SYS_EXISTS", "订单已存在,请仔细核对订单信息"),

    /**
     * 非预计订单状态.
     */
    UNEXPECTED_PAY_STATUS("UNEXPECTED_PAY_STATUS", "非预计订单状态"),

    /**
     * 非预计的数据库更新数量.
     */
    UNEXPECTED_UPDATE_CNT("UNEXPECTED_UPDATE_CNT", "非预计的数据库更新数量"),
    ;

    /**
     * 响应结果码..
     */
    private final String respCode;

    /**
     * 响应结果信息.
     */
    private final String respDesc;
}
