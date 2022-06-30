package paydemo.manager.db;

import paydemo.common.ExecStatusEnum;
import paydemo.common.SysConstant;
import paydemo.dao.dbmodel.PayJobDetailDO;

import java.util.Date;

/**
 * @auther YDXiaa
 * <p>
 * 支付作业详情单创建类.
 */
public class PayJobDetailCreator {


    /**
     * 构建支付作业详情单.
     *
     * @param jobDetailId 作业详情单id.
     * @param jobType     任务类型.
     * @return 作业详情单.
     */
    public static PayJobDetailDO createPayJobDetailDO(String jobDetailId, String jobType) {
        return createPayJobDetailDO(jobDetailId, jobType, SysConstant.JOB_DEFAULT_EXECUTE_INTERVAL);
    }

    /**
     * 构建支付作业详情单.
     *
     * @param jobDetailId        作业详情单id.
     * @param jobType            任务类型.
     * @param jobExecuteInterval 任务执行间隔 单位 s.
     * @return 作业详情单.
     */
    public static PayJobDetailDO createPayJobDetailDO(String jobDetailId, String jobType, Long jobExecuteInterval) {
        return createPayJobDetailDO(jobDetailId, jobType, jobExecuteInterval, SysConstant.JOB_DEFAULT_EXECUTE_TIMES);
    }


    /**
     * 构建支付作业详情单.
     *
     * @param jobDetailId        作业详情单id.
     * @param jobType            任务类型.
     * @param jobExecuteInterval 任务执行间隔 单位 s.
     * @param maxRetry           最大重试次数.
     * @return 作业详情单.
     */
    public static PayJobDetailDO createPayJobDetailDO(String jobDetailId, String jobType, Long jobExecuteInterval, Long maxRetry) {

        PayJobDetailDO payJobDetailDO = new PayJobDetailDO();

        payJobDetailDO.setJobDetailId(jobDetailId);
        payJobDetailDO.setJobType(jobType);
        payJobDetailDO.setExecInterval(jobExecuteInterval);
        payJobDetailDO.setFailRetryTimes(maxRetry);
        payJobDetailDO.setShardingMark(1L); // todo 分片标记暂时分为1片.
        payJobDetailDO.setExecTimes(0L);
        payJobDetailDO.setNextExecTime(new Date());
        payJobDetailDO.setExecStatus(ExecStatusEnum.READY.getExecStatusCode());
        payJobDetailDO.setCreateUser(SysConstant.SYS_USER);
        payJobDetailDO.setCreateDate(new Date());
        payJobDetailDO.setUpdateUser(SysConstant.SYS_USER);
        payJobDetailDO.setUpdateDate(new Date());

        return payJobDetailDO;
    }
}
