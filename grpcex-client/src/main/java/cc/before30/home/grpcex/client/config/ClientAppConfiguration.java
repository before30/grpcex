package cc.before30.home.grpcex.client.config;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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

    @Bean
    public ManagedChannel channel() {
        return ManagedChannelBuilder
                .forAddress("127.0.0.1", 6556)
                .usePlaintext()
                .build();
    }

}
