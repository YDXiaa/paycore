package paydemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.ErrorProcessStatusEnum;
import paydemo.common.ErrorProcessTypeEnum;
import paydemo.common.ErrorTypeEnum;
import paydemo.dao.dbmodel.PayErrorDO;
import paydemo.manager.db.PayErrorManager;

/**
 * @auther YDXiaa
 * <p>
 * 支付差错处理.
 */
public class PayErrorTest extends BaseSpringBootSupport {

    @Autowired
    private PayErrorManager payErrorManager;

    @Test
    public void addPayError(){

        PayErrorDO payErrorDO = new PayErrorDO();

        payErrorDO.setPayNo("123");
        payErrorDO.setPayFundNo("123456");
        payErrorDO.setErrorType(ErrorTypeEnum.REVOKE_FAIL.getErrorTypeCode());
        payErrorDO.setErrorProcessType(ErrorProcessTypeEnum.QUERY_RESULT.getErrorProcessTypeCode());
        payErrorDO.setErrorProcessStatus(ErrorProcessStatusEnum.WAIT_PROCESS.getErrorProcessStatusCode());

        payErrorManager.addPayError(payErrorDO);


    }

}
