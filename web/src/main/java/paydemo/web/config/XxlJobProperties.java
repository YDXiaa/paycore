package paydemo.web.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther YDXiaa
 * <p>
 * xxlJob config.
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "xxl.job")
@EnableConfigurationProperties(value = XxlJobProperties.class)
public class XxlJobProperties {

    private String adminAddresses;

    private String accessToken;

    private String appname;

    private String address;

    private String ip;

    private int port;

    private String logPath;

    private int logRetentionDays;

}
