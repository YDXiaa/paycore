package paydemo.manager.remote.thirdpay;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import paydemo.common.SysConstant;
import paydemo.util.KeyCreator;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @auther YDXiaa
 * <p>
 * 三方支付ServiceFactory.
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class ThirdPayServiceFactory {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * baseRomoteServiceAdapter.
     */
    static final Map<String, BaseRemoteServiceAdapter<?, ?>> SERVICE_FACTORY = Maps.newConcurrentMap();


    /**
     * Init ServiceFactory.
     */
    @PostConstruct
    public void initServiceFactory() {
        applicationContext.getBeansOfType(BaseRemoteServiceAdapter.class).forEach((k, v) -> {
            String key = KeyCreator.createKey(v.getClass().getCanonicalName(), SysConstant.FIXED_SERVICE_NAME);
            SERVICE_FACTORY.put(key, v);
        });
    }

    /**
     * 获取任务执行器.
     *
     * @param key serviceKey.
     * @return serviceAdapter.
     */
    public BaseRemoteServiceAdapter getServiceAdapter(String key) {
        return SERVICE_FACTORY.get(key);
    }
}
