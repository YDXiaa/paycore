package paydemo.manager.remote.thirdpay;

import org.apache.dubbo.rpc.RpcException;
import paydemo.common.RemotePayResult;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.manager.model.RemoteRequestModel;
import paydemo.paygateway.facade.model.GatewayPayResponse;
import paydemo.util.PayStatusEnum;

/**
 * @auther YDXiaa
 * <p>
 * 远程服务适配器,适配所有服务接口.
 */
public interface BaseRemoteServiceAdapter<T, R extends GatewayPayResponse> {

    /**
     * 服务请求对象.
     *
     * @param model model.
     * @return R request.
     */
    T createServiceReq(RemoteRequestModel model);

    /**
     * 服务请求.
     *
     * @param model model.
     * @return 远程结果响应.
     */
    default RemotePayResult service(RemoteRequestModel model) {
        T request = createServiceReq(model);
        R response = doService(request);
        return createServiceResp(model, response);
    }

    /**
     * 远程服务调用(可以自由使用dubbo、springcloud自己实现).
     *
     * @param request request.
     * @return 远程结果.
     */
    R doService(T request);

    /**
     * 响应对象转化.
     *
     * @param response result.
     * @return payResponse.
     */
    RemotePayResult createServiceResp(RemoteRequestModel model, R response);


    /**
     * 调用超时判断.
     *
     * @param throwable 异常信息.
     * @return 是否调用超时.
     */
    default boolean invokeTimeOut(Throwable throwable) {

        if (throwable instanceof RpcException) {
            RpcException rpcException = (RpcException) throwable;
            return rpcException.isTimeout();
        }

        return false;
    }


    /**
     * 调用异常处理(收单业务场景exceptionFail异常当成失败).
     *
     * @param throwable 异常信息.
     * @return 网关支付响应对象.
     */
    default GatewayPayResponse invokeException(Throwable throwable) {
        return invokeException(throwable, true);
    }

    /**
     * 调用异常处理.
     *
     * @param throwable     异常信息.
     * @param exceptionFail 非超时调用异常当成失败处理.
     * @return 网关支付响应对象.
     */
    default GatewayPayResponse invokeException(Throwable throwable, boolean exceptionFail) {

        GatewayPayResponse payResponse = new GatewayPayResponse();

        if (invokeTimeOut(throwable)) {
            payResponse.setPayStatus(PayStatusEnum.PAYING.getStatusCode());
            payResponse.setResultCode(ResponseCodeEnum.REQUEST_CHANNEL_TIMEOUT.getRespCode());
            payResponse.setResultDesc(ResponseCodeEnum.REQUEST_CHANNEL_TIMEOUT.getRespDesc());
        } else {
            // 异常失败.
            if (exceptionFail) {
                payResponse.setPayStatus(PayStatusEnum.FAIL.getStatusCode());
            } else {
                payResponse.setPayStatus(PayStatusEnum.PAYING.getStatusCode());
            }

            payResponse.setResultCode(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespCode());
            payResponse.setResultDesc(ResponseCodeEnum.REQUEST_CHANNEL_EXCEPTION.getRespDesc());
        }

        return payResponse;
    }
}
