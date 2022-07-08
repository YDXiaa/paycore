package paydemo.manager.model;

import lombok.Data;

/**
 * @auther YDXiaa
 * 远程请求Model.
 */
@Data
public class RemoteRequestModel {

    /**
     * 全路径包名.
     */
    private String classPackageName;

    /**
     * 方法名.
     */
    private String methodName;

    /**
     * 请求渠道流水号.
     */
    private String outRequestSeqNo;

    /**
     * 渠道返回流水号.
     */
    private String outRespSeqNo;

    /**
     * 支付金额.
     */
    private Long payAmt;

}
