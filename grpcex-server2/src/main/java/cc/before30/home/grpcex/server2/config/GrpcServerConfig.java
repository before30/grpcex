package cc.before30.home.grpcex.server2.config;

import cc.before30.home.grpcex.server2.service.GreeterService;
import cc.before30.home.grpcex.server2.service.GrpcServerRunner;
import cc.before30.home.grpcex.server2.service.ReactiveGreeterService;
import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import io.grpc.services.HealthStatusManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * GrpcServerConfig
 *
 * @author before30
 * @since 2019-07-09
 */

@Configuration
public class GrpcServerConfig {
    @Bean
    @ConfigurationProperties(prefix="nonrx.grpc")
    public GrpcServerProperties grpcServerProperties() {
        return new GrpcServerProperties();
    }

    @Bean
    public HealthStatusManager grpcServerHealthStatusManager() {
        return new HealthStatusManager();
    }

    @Bean
    public Executor grpcServerExecutor() {
        return ForkJoinPool.commonPool();
    }

    @Bean
    public GrpcServerRunner grpcServerRunner(@Qualifier("grpcServerHealthStatusManager") HealthStatusManager healthStatusManager,
                                             @Qualifier("grpcServerProperties") GrpcServerProperties grpcServerProperties,
                                             @Qualifier("grpcServerExecutor") Executor executor) {
        List<BindableService> services = new ArrayList<>();
        services.add(new GreeterService());
        return new GrpcServerRunner(healthStatusManager,
                grpcServerProperties,
                ServerBuilder.forPort(grpcServerProperties.getPort()),
                services,
                executor);
    }
}
