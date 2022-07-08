package paydemo.dao.dbmodel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 渠道基本信息.
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("T_CHANNEL_INFO")
public class ChannelInfoDO extends BaseDO {

    /**
     * 渠道编码(例如支付宝 ALIPAY).
     */
    @TableField("channel_no")
    private String channelNo;

    /**
     * 渠道自定义名称.
     */
    @TableField("channel_name")
    private String channelName;

    /**
     * 渠道描述备注.
     */
    @TableField("channel_desc")
    private String channelDesc;

    /**
     * 渠道对接联系人姓名.
     */
    @TableField("channel_contact_name")
    private String channelContactName;

    /**
     * 渠道对接联系人电话.
     */
    @TableField("channel_contact_phone")
    private String channelContactPhone;

    /**
     * 渠道路由比例(新通道开启比例) 渠道排序的时候使用.
     */
    @TableField("channel_weight")
    private Long channelWeight;

    /**
     * 渠道维护描述(用于收银台展示).
     */
    @TableField("no_service_desc")
    private String noServiceDesc;

    /**
     * 渠道服务维护开始时间.
     */
    @TableField("no_service_begin_time")
    private Date noServiceBeginTime;

    /**
     * 渠道服务维护结束时间.
     */
    @TableField("no_service_end_time")
    private Date noServiceEndTime;

    /**
     * 渠道状态 OPEN/CLOSE (收银台不展示，收银台展示使用noServiceFlag判断).
     */
    @TableField("channel_status")
    private String channelStatus;

}
