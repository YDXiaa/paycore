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
     * 订单不存在.
     */
    ORDER_NOT_EXISTS("ORDER_NOT_EXISTS","订单已存在,请仔细核对订单信息"),

    /**
     * 非预计订单状态.
     */
    UNEXPECTED_PAY_STATUS("UNEXPECTED_PAY_STATUS", "非预计订单状态"),

    /**
     * 非预计的数据库更新数量.
     */
    UNEXPECTED_UPDATE_CNT("UNEXPECTED_UPDATE_CNT", "非预计的数据库更新数量"),

    /**
     * 可用路由渠道不存在.
     */
    USABLE_CHANNEL_NOT_EXISTS("USABLE_CHANNEL_NOT_EXISTS", "路由渠道为空,请重新选择支付方式"),

    /**
     * 请求渠道超时.
     */
    REQUEST_CHANNEL_TIMEOUT("REQUEST_CHANNEL_TIMEOUT", "请求渠道超时"),

    /**
     * 请求渠道异常.
     */
    REQUEST_CHANNEL_EXCEPTION("REQUEST_CHANNEL_EXCEPTION", "请求渠道异常"),

    /**
     * 手续费计算模式错误.
     */
    FEE_CAL_MODE_ERROR("FEE_CAL_MODE_ERROR", "手续费计算模式错误"),

    /**
     * 支付状态不支持此操作.
     */
    PAY_STATUS_NOT_SUPPORT("PAY_STATUS_NOT_SUPPORT", "支付状态不支持此操作"),

    /**
     * 退款金额超出原单金额.
     */
    REFUND_AMT_OVERMUCH("REFUND_AMT_OVERMUCH", "退款金额超出可退款金额"),

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
