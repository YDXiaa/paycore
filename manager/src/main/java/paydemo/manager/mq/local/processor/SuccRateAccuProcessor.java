package paydemo.manager.mq.local.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paydemo.manager.helper.ChannelDetailAccumulateHelper;
import paydemo.manager.mq.local.event.PayResultEvent;

/**
 * @auther YDXiaa
 * <p>
 * 成功率累计.
 */
@Slf4j
@Component
public class SuccRateAccuProcessor extends AbstractLocalEventProcessor<PayResultEvent> {

    @Autowired
    private ChannelDetailAccumulateHelper accumulateHelper;

    @Override
    protected void process(PayResultEvent event) {

        log.info("收到PayResultEvent,消息体:{}", event);
        // 成功率累计.
        try {
//            accumulateHelper.accumulateChannelDetail(event.getPayStatus(), event.getChannelDetailNo());
        } catch (Throwable throwable) {
            log.error("渠道交易累加异常,异常信息:", throwable);
        }
    }
}
