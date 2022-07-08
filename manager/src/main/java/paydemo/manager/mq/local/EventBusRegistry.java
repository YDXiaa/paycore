package paydemo.manager.mq.local;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import paydemo.manager.mq.local.processor.AbstractLocalEventProcessor;

import java.util.Map;

/**
 * @auther YDXiaa
 * <p>
 * eventBus registry.
 */
@Component
@SuppressWarnings("all")
public class EventBusRegistry implements InitializingBean, DisposableBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AsyncEventBus asyncEventBus;

    // processorMap.
    private Map<String, AbstractLocalEventProcessor> processorMap = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        processorMap = applicationContext.getBeansOfType(AbstractLocalEventProcessor.class);
        processorMap.forEach((k, v) -> {
            asyncEventBus.register(v);
        });
    }

    @Override
    public void destroy() throws Exception {
        processorMap.forEach((k, v) -> {
            asyncEventBus.unregister(v);
        });
    }

}
