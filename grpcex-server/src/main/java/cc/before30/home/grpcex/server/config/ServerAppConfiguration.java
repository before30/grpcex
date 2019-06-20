package cc.before30.home.grpcex.server.config;

import cc.before30.home.grpcex.server.service.GreeterService;
import com.google.common.collect.Maps;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
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


    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient eurekaClient;

    @Value("${grpc.port:6556}")
    private int grpcPort;

    @PostConstruct
    private void init() {
        Map<String, String> map = Maps.newHashMap();
        map.put("grpc.port", String.valueOf(grpcPort));
        eurekaClient.getApplicationInfoManager().registerAppMetadata(map);
    }
}
