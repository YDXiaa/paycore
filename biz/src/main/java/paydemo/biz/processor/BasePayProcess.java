package paydemo.biz.processor;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.VerifyUtil;
import paydemo.manager.db.PayFundManager;
import paydemo.manager.model.PayFundBO;
import paydemo.util.PayStatusEnum;

/**
 * @auther YDXiaa
 * <p>
 * 公共支付处理.
 */
@Service
public class BasePayProcess {

    @Autowired
    private PayFundManager payFundManager;


    /**
     * 支付前支付处理.
     *
     * @param payFundBO 支付资金单.
     */
    public void payBeforePayStatusProcess(PayFundBO payFundBO) {

        // 更新处理中.
        payFundBO.setPayStatus(PayStatusEnum.PAYING.getStatusCode());
        // 更新支付状态为PAYING.
        int moidfyResult = payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.INIT);
        VerifyUtil.verifySqlResult(moidfyResult);

    }


    /**
     * 支付结果支付处理.
     *
     * @param payFundBO 支付资金单.
     */
    public void payResultStatusProcess(PayFundBO payFundBO) {
        // 更新支付结果.
        payFundManager.modifyPayFundStatus(payFundBO, PayStatusEnum.PAYING);
    }


}
