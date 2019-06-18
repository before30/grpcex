package cc.before30.home.grpcex.server.config;

import io.grpc.ServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * MyGrpcServerBuilderConfigurer
 *
 * @author before30
 * @since 2019-06-09
 */

@Component
public class MyGrpcServerBuilderConfigurer extends GRpcServerBuilderConfigurer {

    private final Executor executor;

    public MyGrpcServerBuilderConfigurer(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
        serverBuilder
                .executor(executor);
    }
}
