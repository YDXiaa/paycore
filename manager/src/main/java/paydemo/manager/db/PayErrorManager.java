package paydemo.manager.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paydemo.dao.dbmodel.PayErrorDO;
import paydemo.dao.mapper.PayErrorMapper;

/**
 * @auther YDXiaa
 */
@Repository
public class PayErrorManager {

    @Autowired
    private PayErrorMapper payErrorMapper;

    /**
     * 添加支付差错记录单.
     *
     * @param payErrorDO 差错DO.
     * @return 添加结果.
     */
    public boolean addPayError(PayErrorDO payErrorDO) {
        return 1 == payErrorMapper.insert(payErrorDO);
    }


}
