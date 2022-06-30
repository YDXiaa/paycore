package paydemo.web.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther YDXiaa
 * <p>
 * xxlJob configuration.
 */
@Configuration
public class XxlJobConfiguration {

    /**
     * init xxlJobExecutor Copy from XXL-JOB SpringBoot samples.
     *
     * @return exceutor.
     */
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties xxlJobProperties) {

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();

        // 中心amdin连接地址.
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        // appName.
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppname());
        // 节点地址.
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
        // 节点ip.
        xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        // 节点port.
        xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        // admin adressToken.
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        // log cofnig.
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        // 日志保留时间.
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());

        return xxlJobSpringExecutor;
    }

}
