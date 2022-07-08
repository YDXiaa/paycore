package paydemo.manager.mq.local.processor;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import paydemo.manager.mq.local.event.BaseLocalEvent;

/**
 * @auther YDXiaa
 * <p>
 * 消息事件适配器.
 */
@Slf4j
@SuppressWarnings("all")
public abstract class AbstractLocalEventProcessor<T extends BaseLocalEvent> {

    @Subscribe
    public void listen(T event) {
        try {
            process(event);
        } catch (Throwable throwable) {

        }
    }

    /**
     * 处理消息事件.
     *
     * @param event event.
     */
    protected abstract void process(T event);
}
