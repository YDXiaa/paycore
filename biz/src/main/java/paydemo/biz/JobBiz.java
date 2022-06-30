package paydemo.biz;

import org.springframework.stereotype.Service;
import paydemo.dao.dbmodel.PayJobDetailDO;


/**
 * @auther YDXiaa
 * <p>
 * job biz.
 */
@Service
public class JobBiz {


    /**
     * job处理.
     *
     * @param payJobDetailDO 支付单.
     * @return 处理结果.
     */
    public boolean process(PayJobDetailDO payJobDetailDO) {

        return true;
    }
}
