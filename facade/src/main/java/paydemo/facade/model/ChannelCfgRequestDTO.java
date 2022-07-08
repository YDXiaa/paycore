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
public class ChannelCfgRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 渠道编码(例如支付宝 ALIPAY).
     */
    private String channelNo;

    /**
     * 渠道自定义名称.
     */
    private String channelName;

    /**
     * 渠道描述备注.
     */
    private String channelDesc;

    /**
     * 渠道对接联系人姓名.
     */
    private String channelContactName;

    /**
     * 渠道对接联系人电话.
     */
    private String channelContactPhone;

    /**
     * 渠道路由比例 -1L不指定.
     */
    private Long channelWeight;

    /**
     * 渠道系统不可用标识 TRUE 渠道不可用,FALSE渠道可用.
     */
    private String noServiceFlag;

    /**
     * 渠道不服务原因(用于收银台展示).
     */
    private String noServiceReason;

    /**
     * 渠道不服务开始时间.
     */
    private Date noServiceBeginTime;

    /**
     * 渠道不服务结束时间.
     */
    private Date noServiceEndTime;

    /**
     * 渠道状态 OPEN/CLOSE.
     */
    private String channelStatus;
}
