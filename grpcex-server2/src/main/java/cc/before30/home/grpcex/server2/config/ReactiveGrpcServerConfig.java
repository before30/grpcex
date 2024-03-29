package cc.before30.home.grpcex.server2.config;

import cc.before30.home.grpcex.server2.service.GrpcServerRunner;
import cc.before30.home.grpcex.server2.service.ReactiveGreeterService;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import java.util.concurrent.Executors;

/**
 * ReactiveGrpcServerConfig
 *
 * @author before30
 * @since 2019-07-08
 */

@Configuration
public class ReactiveGrpcServerConfig {

    @Bean
    @ConfigurationProperties(prefix="rx.grpc")
    public GrpcServerProperties reactiveGrpcServerProperties() {
        return new GrpcServerProperties();
    }

    @Bean
    public HealthStatusManager reactiveGrpcServerHealthStatusManager() {
        return new HealthStatusManager();
    }

    @Bean
    public Executor reactiveGrpcServerExecutor() {
        return Executors.newFixedThreadPool(10,
                new ThreadFactoryBuilder().setNameFormat("reactive-thread-%d").build());
    }

    @Bean
    public GrpcServerRunner reactiveGrpcServerRunner(@Qualifier("reactiveGrpcServerHealthStatusManager") HealthStatusManager healthStatusManager,
                                                     @Qualifier("reactiveGrpcServerProperties") GrpcServerProperties grpcServerProperties,
                                                     @Qualifier("reactiveGrpcServerExecutor") Executor executor) {
        List<BindableService> services = new ArrayList<>();
        services.add(new ReactiveGreeterService());
        return new GrpcServerRunner(healthStatusManager,
                grpcServerProperties,
                ServerBuilder.forPort(grpcServerProperties.getPort()),
                services,
                executor);
    }
}
