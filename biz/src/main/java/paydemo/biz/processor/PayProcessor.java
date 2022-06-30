package paydemo.biz.processor;


import paydemo.common.RemotePayResult;
import paydemo.manager.model.PayFundBO;

/**
 * @auther YDXiaa
 * <p>
 * 支付处理器.
 */
public interface PayProcessor {

    /**
     * 支付.
     *
     * @param payFundBO 支付资金单.
     * @return 资金系统处理结果.
     */
    RemotePayResult pay(PayFundBO payFundBO);

    /**
     * 冲正.
     *
     * @param payFundBO 支付资金单.
     * @return 资金系统处理结果.
     */
    RemotePayResult revoke(PayFundBO payFundBO);
}
