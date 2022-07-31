package paydemo.manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @auther YDXiaa
 * <p>
 * 支付基础信息信息.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayBaseInfoBO {

    /**
     * 支付金额.
     */
    private long payAmt;

    /**
     * 支付类型(优先选择支付类型.).
     *
     */
    private String payType;

    /**
     * 支付类型集合.
     */
    private List<String> availablePayTypes;

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
