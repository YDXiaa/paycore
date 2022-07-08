package paydemo.web.config;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @auther YDXiaa
 * <p>
 * guava eventBus事件配置.
 */
@Configuration
public class EventBusConfiguration {

    /**
     * eventBusBean.
     *
     * @return eventBus.
     */
    @Bean
    public AsyncEventBus asyncEventBus() {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 50,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000));

        AsyncEventBus eventBus = new AsyncEventBus(threadPoolExecutor);

        return eventBus;
    }
}
