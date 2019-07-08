package cc.before30.home.grpcex.server2.config;

import cc.before30.home.grpcex.server2.service.GrpcServerRunner;
import cc.before30.home.grpcex.server2.service.ReactiveGreeterService;
import com.google.common.collect.Lists;
import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import io.grpc.services.HealthStatusManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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
    public GrpcServerRunner reactiveGrpcServerRunner(HealthStatusManager healthStatusManager,
                                                     GrpcServerProperties grpcServerProperties) {
        List<BindableService> services = new ArrayList<>();
        services.add(new ReactiveGreeterService());
        return new GrpcServerRunner(healthStatusManager,
                grpcServerProperties,
                ServerBuilder.forPort(grpcServerProperties.getPort()),
                services);
    }
}
