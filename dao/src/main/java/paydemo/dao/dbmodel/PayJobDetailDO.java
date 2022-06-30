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
 * xxl-job支付作业表.
 */
@Getter
@Setter
@ToString
@TableName("T_PAY_JOB_DETAIL")
public class PayJobDetailDO extends BaseDO {

    /**
     * 作业id.
     */
    @TableField("job_detail_id")
    private String jobDetailId;

    /**
     * 作业类型.
     */
    @TableField("job_type")
    private String jobType;

    /**
     * 任务间隔多长时间执行一次单位秒.
     */
    @TableField("exec_interval")
    private Long execInterval;

    /**
     * 失败重试次数.
     */
    @TableField("fail_retry_times")
    private Long failRetryTimes;

    /**
     * 执行失败已经重试次数.
     */
    @TableField("exec_times")
    private Long execTimes;

    /**
     * 下一次执行时间.
     */
    @TableField("next_exec_time")
    private Date nextExecTime;

    /**
     * 分片作业标记(xxl-job分片作业使用).
     */
    @TableField("sharding_mark")
    private Long shardingMark;

    /**
     * 执行状态.
     */
    @TableField("exec_status")
    private String execStatus;

}
