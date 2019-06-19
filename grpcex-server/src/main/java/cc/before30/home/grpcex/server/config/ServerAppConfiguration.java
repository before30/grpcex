package cc.before30.home.grpcex.server.config;

import cc.before30.home.grpcex.server.service.GreeterService;
import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * ServerAppConfiguration
 *
 * @author before30
 * @since 2019-06-19
 */
@Configuration
public class ServerAppConfiguration {

    @Bean
    public Executor executor() {
        return ForkJoinPool.commonPool();
    }

//    @Bean
//    public GreeterService greeterService() {
//        return new GreeterService();
//    }

}
