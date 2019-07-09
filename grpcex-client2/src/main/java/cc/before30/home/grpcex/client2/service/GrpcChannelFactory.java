package cc.before30.home.grpcex.client2.service;

import cc.before30.home.grpcex.client2.config.GrpcClientProperties;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

/**
 * GrpcChannelFactory
 *
 * @author before30
 * @since 2019-07-09
 */

@Slf4j
@UtilityClass
public class GrpcChannelFactory {

    public ManagedChannel createGrpcChannel(@Nonnull final GrpcClientProperties properties) {
        return ManagedChannelBuilder
                .forAddress(properties.getAddress(), properties.getPort())
                .usePlaintext()
                .build();
    }

}
