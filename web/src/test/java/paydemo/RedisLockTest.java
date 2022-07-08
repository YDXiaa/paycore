package paydemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.facade.model.PayRequestDTO;
import paydemo.manager.redis.RedisKit;
import paydemo.service.PayServiceImpl;

import java.util.List;
import java.util.UUID;

/**
 * @auther YDXiaa
 *
 * redisLockTest.
 */
public class RedisLockTest extends BaseSpringBootSupport{

    @Autowired
    private PayServiceImpl payServiceImpl;

    @Autowired
    private RedisKit redisKit;


    @Test
    public void testLock(){
        PayRequestDTO payRequestDTO = new PayRequestDTO();
        payRequestDTO.setRequestBizNo(UUID.randomUUID().toString());
        payRequestDTO.setBizLine("MALL");

        payServiceImpl.pay(payRequestDTO);
    }

    @Test
    public void testHash(){
        redisKit.saveCache4Hash("testHash","successCnt",1L,600L);
        redisKit.saveCache4Hash("testHash","failCnt",1L,600L);

        List<Object> hash = redisKit.findCache4Hash("testHash", "successCnt", "failCnt","payingCnt");

        System.out.println(hash.size());
        hash.forEach(System.out::println);
    }


}
