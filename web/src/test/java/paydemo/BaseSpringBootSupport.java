package paydemo;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import paydemo.web.PayAppBootStrap;

/**
 * @auther YDXiaa
 * <p>
 * SpringBoot测试基类.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayAppBootStrap.class)
public class BaseSpringBootSupport {

}
