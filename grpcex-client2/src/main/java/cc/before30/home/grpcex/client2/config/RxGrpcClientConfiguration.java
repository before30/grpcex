package cc.before30.home.grpcex.client2.config;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpcex.client2.service.GrpcChannelFactory;
import cc.before30.home.grpcex.client2.service.GrpcClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RxGrpcClientConfiguration
 *
 * @author before30
 * @since 2019-07-09
 */

@Configuration
public class RxGrpcClientConfiguration {

    @ConfigurationProperties(prefix = "rx.grpc")
    @Bean
    public GrpcClientProperties rxGrpcClientProperties() {
        return new GrpcClientProperties();
    }

    @Bean
    public ManagedChannel rxGrpcManagedChannel(@Qualifier("rxGrpcClientProperties") GrpcClientProperties properties) {
        return GrpcChannelFactory.createGrpcChannel(properties);
    }

    @Bean
    public GrpcClient grpcMultiSayHelloClient(@Qualifier("rxGrpcManagedChannel") ManagedChannel channel) {
        return new GrpcClient(GreeterGrpc.getMultiSayHelloMethod(), channel);
    }

    @Bean
    public GrpcClient grpcSayHelloClient(@Qualifier("rxGrpcManagedChannel") ManagedChannel channel) {
        return new GrpcClient(GreeterGrpc.getSayHelloMethod(), channel);
    }

    @Bean
    public GrpcClient grpcStreamSayHelloClient(@Qualifier("rxGrpcManagedChannel") ManagedChannel channel) {
        return new GrpcClient(GreeterGrpc.getStreamSayHelloMethod(), channel);
    }
}
