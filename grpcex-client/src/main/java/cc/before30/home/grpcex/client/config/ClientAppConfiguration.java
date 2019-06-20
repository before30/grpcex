package cc.before30.home.grpcex.client.config;

import cc.before30.home.grpcex.client.resolver.EurekaNameResolverProvider;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import io.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * ClientAppConfiguration
 *
 * @author before30
 * @since 2019-06-19
 */

@Configuration
public class ClientAppConfiguration {

    @Bean
    public Executor executor() {
        return ForkJoinPool.commonPool();
    }


    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient eurekaClient;

    @Bean
    public ManagedChannel channel() {
        return ManagedChannelBuilder
                .forTarget("eureka://grpc_server")
                .nameResolverFactory(new EurekaNameResolverProvider(eurekaClient))
                .usePlaintext()
                .build();
    }

}
