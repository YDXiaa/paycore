package paydemo.biz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.JobTypeEnum;
import paydemo.dao.dbmodel.PayJobDetailDO;


/**
 * @auther YDXiaa
 * <p>
 * job biz.
 */
@Slf4j
@Service
public class JobBiz {

    @Autowired
    private MqRetrySendBiz mqRetrySendBiz;

    @Autowired
    private PayRevokeBiz payRevokeBiz;


    /**
     * job处理.
     *
     * @param payJobDetailDO 支付单.
     * @return 处理结果.
     */
    public boolean process(PayJobDetailDO payJobDetailDO) {

        log.info("Job任务处理开始,作业单信息:{}",payJobDetailDO);

        JobTypeEnum jobTypeEnum = JobTypeEnum.match(payJobDetailDO.getJobType());

        if (null == jobTypeEnum) {
            return false;
        }

        switch (jobTypeEnum) {
            case PAY_REVOKE:
                return payRevokeBiz.revoke(payJobDetailDO.getJobDetailId());

            case MQ_RETRY_SEND:
                return mqRetrySendBiz.sendMQMessage(payJobDetailDO.getJobDetailId());

            default:
                return true;
        }
    }
}
