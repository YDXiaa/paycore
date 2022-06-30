package paydemo.web.job;

import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.DateUtil;
import paydemo.common.ExecStatusEnum;
import paydemo.common.VerifyUtil;
import paydemo.dao.dbmodel.PayJobDetailDO;
import paydemo.manager.db.PayJobDetailManager;

import java.util.Date;
import java.util.List;

/**
 * @auther YDXiaa
 * <p>
 * 统一任务调度模板类.
 */
@Slf4j
public abstract class BaseJobSchedule {

    // 读取数据库数量.
    static final int defaultReadCnt = 100;

    @Autowired
    private PayJobDetailManager payJobDetailManager;

    /**
     * 统一调度核心逻辑入口.
     * <p>
     * todo 异常 作业单状态无法更新,无法继续下去,需要由手动推进入口.
     */
    public void scheduleForSharding() {

        // xxl-job获取 shardIndex.
        int shardingMark = XxlJobHelper.getShardIndex();

        // 查询任务Jobl列表.
        List<PayJobDetailDO> payJobDetailDOList = payJobDetailManager.queryPayJobDetailList(shardingMark, getReadCount());

        // 处理.
        payJobDetailDOList.forEach(payJobDetailDO -> {

            // 作业单更新为处理中.
            schedulePreProcess2Run(payJobDetailDO);

            // 执行结果.
            boolean executeReult = process(payJobDetailDO);

            // 执行结果处理.
            if (executeReult) {
                scheduleExecuteResult2Suc(payJobDetailDO);
            } else if (payJobDetailDO.getExecTimes() < payJobDetailDO.getFailRetryTimes()) {
                scheduleExecuteResult2Retry(payJobDetailDO);
            } else {
                scheduleExecuteResult2Fail(payJobDetailDO);
            }

            // 刷新执行结果.
            refreshScheduleExecuteResult(payJobDetailDO);
        });
    }

    /**
     * 刷新执行结果.
     *
     * @param payJobDetailDO 作业任务单.
     */
    private void refreshScheduleExecuteResult(PayJobDetailDO payJobDetailDO) {

        int modifyCnt = payJobDetailManager.modifyPayJobDetailStatus(payJobDetailDO);

        VerifyUtil.verifySqlResult(modifyCnt);
    }

    /**
     * 预处理作业单状态.
     *
     * @param payJobDetailDO 作业任务单.
     */
    private void schedulePreProcess2Run(PayJobDetailDO payJobDetailDO) {
        payJobDetailDO.setExecStatus(ExecStatusEnum.WORKING.getExecStatusCode());
        payJobDetailDO.setExecTimes(payJobDetailDO.getExecTimes() + 1);
    }


    /**
     * 任务执行失败.
     *
     * @param payJobDetailDO 作业任务单.
     */
    private void scheduleExecuteResult2Fail(PayJobDetailDO payJobDetailDO) {
        payJobDetailDO.setExecStatus(ExecStatusEnum.FAIL.getExecStatusCode());
        // todo 差错处理逻辑.
    }

    /**
     * 任务执行失败在没有超过执行次数场景下可以重试.
     *
     * @param payJobDetailDO 作业任务单.
     */
    private void scheduleExecuteResult2Retry(PayJobDetailDO payJobDetailDO) {
        payJobDetailDO.setExecStatus(ExecStatusEnum.READY.getExecStatusCode());
        payJobDetailDO.setNextExecTime(DateUtil.after(new Date(),payJobDetailDO.getExecInterval()));
    }

    /**
     * 任务执行结果.
     *
     * @param payJobDetailDO 作业任务单.
     */
    private void scheduleExecuteResult2Suc(PayJobDetailDO payJobDetailDO) {
        payJobDetailDO.setExecStatus(ExecStatusEnum.SUCCESS.getExecStatusCode());
    }

    /**
     * 核心执行方法.
     *
     * @param payJobDetailDO 作业任务单.
     * @return 处理结果.
     */
    protected abstract boolean process(PayJobDetailDO payJobDetailDO);

    /**
     * 批次读取数量，子类实现.
     *
     * @return 读取数量.
     */
    protected int getReadCount() {
        return defaultReadCnt;
    }

}
