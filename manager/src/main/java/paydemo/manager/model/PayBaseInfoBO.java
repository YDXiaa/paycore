package paydemo.manager.model;

import lombok.Data;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 支付基础信息信息.
 */
@Data
public class PayBaseInfoBO {

    /**
     * 支付金额.
     */
    private long payAmt;

    /**
     * 支付类型.
     *
     * todo 如果是冲正场景,如果渠道提供了撤销接口,应该优先调用撤销接口;如果不存在，调用退款接口.
     * todo 所以此处需要支持传入多张支付类型.
     */
    private String payType;

    /**
     * 支付工具.
     */
    private String payTool;

    /**
     * 支付子工具.
     */
    private String paySubTool;

    /**
     * 业务类型.
     */
    private String bizType;

    /**
     * 重新路由使用,上一次使用路由渠道详情编码.
     */
    private String payFailChannelDetailNo;

    /**
     * 支付单路由渠道编码(退款、查询使用).
     */
    private String payChannelDetailNo;

}
