package paydemo.biz;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paydemo.biz.processor.PayProcessor;
import paydemo.common.PayToolEnum;
import paydemo.common.VerifyUtil;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @auther YDXiaa
 * <p>
 * 支付处理器简单工厂工厂类.
 */
@Component
public class PayProcessorFactory {

    /**
     * 余额支付处理器.
     */
    @Autowired
    private PayProcessor balancePayProcessor;

    /**
     * 第三方支付处理器.
     */
    @Autowired
    private PayProcessor thirdPayProcessor;

    /**
     * 营销支付处理器.
     */
    @Autowired
    private PayProcessor marketingPayProcessor;

    /**
     * processor Factory.
     */
    public Map<String, PayProcessor> factory = Maps.newHashMap();


    @PostConstruct
    public void initFactory() {
        factory.put(PayToolEnum.THIRD_PAY.getPayToolCode(), thirdPayProcessor);
        factory.put(PayToolEnum.BALANCE.getPayToolCode(), balancePayProcessor);
        factory.put(PayToolEnum.MARKETING.getPayToolCode(), marketingPayProcessor);
    }


    /**
     * 获取支付工具处理器.
     *
     * @param payToolCode 支付工具.
     * @return 支付工具处理器.
     */
    public PayProcessor getPayProcessor(String payToolCode) {
        VerifyUtil.verifyRequiredField();
        return factory.get(payToolCode);
    }


}
