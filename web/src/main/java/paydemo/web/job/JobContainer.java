package paydemo.web.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paydemo.biz.JobBiz;
import paydemo.dao.dbmodel.PayJobDetailDO;


/**
 * @auther YDXiaa
 * <p>
 * 用于统一xxl-job管理定时任务容器.
 */
@Slf4j
@Component
public class JobContainer extends BaseJobSchedule {

    @Autowired
    private JobBiz jobBiz;

    /**
     * job process 分片执行提高作业效率.
     * <p>
     */
    @XxlJob(value = "payJobDetailJob")
    public void payJobDetailProcess() {
        // 作业单调度核心.
        super.scheduleForSharding();
    }


    @Override
    protected boolean process(PayJobDetailDO payJobDetailDO) {

        System.out.println("处理任务job:"+ payJobDetailDO.toString());

        return false;
    }
}
