package cc.before30.home.grpcex.client.config;

import cc.before30.home.grpcex.client.resolver.EurekaNameResolverProvider;
import com.netflix.discovery.EurekaClient;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

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

//        return ManagedChannelBuilder
//                .forTarget("eureka://" + eurekaServiceId)
//                .nameResolverFactory(new EurekaNameResolverProvider(eurekaClientConfig, "grpc.port"))
//                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
//                .usePlaintext(true)
//                .build();

    }

}
