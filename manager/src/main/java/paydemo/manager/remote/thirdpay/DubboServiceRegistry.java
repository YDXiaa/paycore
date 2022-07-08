package paydemo.manager.remote.thirdpay;

import com.google.common.collect.Maps;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @auther YDXiaa
 * <p>
 * dubboServiceRegistry.
 */
@Service
@SuppressWarnings("all")
public class DubboServiceRegistry {

//    @Autowired
    private ApplicationConfig applicationConfig;

//    @Autowired
    private RegistryConfig registryConfig;

    /**
     * dubboServiceRegistry.
     */
    public static final Map<Class<?>, Object> DUBBO_SERVICE_CONTAINER = Maps.newConcurrentMap();

    /**
     * 获取dubbo服务实例对象.
     *
     * @param dubboService className.
     * @param <T>          service.
     * @return dubboService.
     */
    public <T> T getDubboService(Class<T> dubboService) {
        Object service = DUBBO_SERVICE_CONTAINER.computeIfAbsent(dubboService, key -> createDubboService(dubboService));
        return (T) service;
    }

    /**
     * 创建dubbo服务.
     *
     * @param dubboService dubboService.
     * @param <T>          Service.
     * @return T .
     */
    private  <T> T createDubboService(Class<T> dubboService) {
        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        reference.setInterface(dubboService);
        return reference.get();
    }


}
