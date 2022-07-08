package paydemo.manager.mq.local.event;

import lombok.*;

/**
 * @auther YDXiaa
 */
@Getter
@Setter
@ToString
public class PayResultEvent extends BaseLocalEvent {

    /**
     * 支付结果.
     */
    private String payStatus;

    /**
     * 渠道详情编码.
     */
    private String channelDetailNo;

    public PayResultEvent(){
        super();
    }

    public PayResultEvent(String payStatus,String channelDetailNo){
        super();
        this.payStatus = payStatus;
        this.channelDetailNo = channelDetailNo;
    }

}
