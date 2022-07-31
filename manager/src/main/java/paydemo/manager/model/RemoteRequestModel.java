package paydemo.manager.model;

import lombok.Builder;
import lombok.Data;
import paydemo.common.PushQueryTypeEnum;

import java.util.Date;

/**
 * @auther YDXiaa
 * 远程请求Model.
 */
@Data
@Builder
public class RemoteRequestModel {

    /**
     * 渠道编码.
     */
    private String channelNo;

    /**
     * 渠道详情编码.
     */
    private String channelDetailNo;

    /**
     * 全路径包名.
     */
    private String classPackageName;

    /**
     * 方法名.
     */
    private String methodName;

    /**
     * 请求渠道流水日期.
     */
    private Date outRequestDate;

    /**
     * 请求渠道流水号.
     */
    private String outRequestSeqNo;

    /**
     * 渠道返回流水号.
     */
    private String outRespSeqNo;

    /**
     * 支付单号.
     */
    private String payNo;

    /**
     * 支付资金单号.
     */
    private String payFundNo;

    /**
     * 支付类型.
     */
    private String payType;

    /**
     * 支付金额.
     */
    private Long payAmt;

    /**
     * 补单查询类型.
     */
    private PushQueryTypeEnum pushQueryTypeEnum;

}
