package paydemo.facade.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 路由渠道配置对象.
 */
@Data
public class ChannelDetailCfgRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道编号.
     */
    private String channelNo;

    /**
     * 渠道详情编号.
     */
    private String channelDetailNo;

    /**
     * 支付类型.
     */
    private String payType;

    /**
     * 业务类型.
     */
    private String bizType;

    /**
     * 支付工具类型.
     */
    private String payTool;

    /**
     * 支付工具子类型.
     */
    private String paySubTool;

    /**
     * 最低金额.
     */
    private Long minPayAmt;

    /**
     * 最大金额.
     */
    private Long maxPayAmt;

    /**
     * SpringBean对应类名.
     */
    private String className;

    /**
     * 全路径包名.
     */
    private String classPackageName;

    /**
     * 参数类型.
     */
    private String parameterType;

    /**
     * 类方法名.
     */
    private String methodName;


    // 渠道收费信息.
    /**
     * 计费标识,TRUE计费,FALSE不计费.
     */
    private String feeFlag;

    /**
     * 计费表示TRUE时有值,计费详细信息.
     */
    private ChannelDetailFeeRequestDTO channelDetailFeeRequestDTO;

}
