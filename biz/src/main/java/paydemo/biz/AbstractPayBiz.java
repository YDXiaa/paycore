package paydemo.biz;

import paydemo.manager.model.PayRequestBO;
import paydemo.manager.model.PayResponseBO;

/**
 * @auther YDXiaa
 * 支付核心流程模板类.
 */
public abstract class AbstractPayBiz<R extends PayRequestBO> {


    /**
     * 核心支付流程处理.
     *
     * @param request 请求对象.
     * @return 响应对象.
     */
    public PayResponseBO execute(R request) {

        PayResponseBO payResponseBO = new PayResponseBO();

        // 支付前置流程处理.
        beforePayProcess(request, payResponseBO);

        // 支付流程处理.
        payProcess(request, payResponseBO);

        // 支付后置流程处理.
        afterPayProcess(request, payResponseBO);

        return payResponseBO;
    }

    /**
     * 支付前置处理.
     *
     * @param request       支付请求对象.
     * @param payResponseBO 支付统一响应对象.
     */
    protected abstract void beforePayProcess(R request, PayResponseBO payResponseBO);

    /**
     * 支付核心流程处理.
     *
     * @param request       支付请求对象.
     * @param payResponseBO 支付统一响应对象.
     */
    protected abstract void payProcess(R request, PayResponseBO payResponseBO);

    /**
     * 支付后置流程处理.
     *
     * @param request       支付统一响应对象.
     * @param payResponseBO 支付统一响应对象.
     */
    protected abstract void afterPayProcess(R request, PayResponseBO payResponseBO);


}
