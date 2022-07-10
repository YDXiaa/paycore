package paydemo.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SringBoot启动入口.
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = "paydemo")
@EnableTransactionManagement
@MapperScan(basePackages = "paydemo.dao.mapper")
public class PayAppBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(PayAppBootStrap.class, args);
    }

}
