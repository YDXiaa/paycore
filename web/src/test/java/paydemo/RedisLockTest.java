package paydemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.facade.model.PayRequestDTO;
import paydemo.service.PayServiceImpl;

import java.util.UUID;

/**
 * @auther YDXiaa
 *
 * redisLockTest.
 */
public class RedisLockTest extends BaseSpringBootSupport{

    @Autowired
    private PayServiceImpl payServiceImpl;


    @Test
    public void testLock(){
        PayRequestDTO payRequestDTO = new PayRequestDTO();
        payRequestDTO.setRequestBizNo(UUID.randomUUID().toString());
        payRequestDTO.setBizLine("MALL");

        payServiceImpl.pay(payRequestDTO);
    }


}
