package cc.before30.home.grpcex.client.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * GreeterService
 *
 * @author before30
 * @since 2019-06-19
 */

@Slf4j
@Component
public class GreeterService {

    private final Executor executor;
    private final GreeterGrpc.GreeterFutureStub stub;

    public GreeterService(Executor executor, Channel channel) {
        this.executor = executor;
        this.stub = GreeterGrpc.newFutureStub(channel);
    }

    public ListenableFuture<GreeterOuterClass.HelloReply> greet(String name) {
        final GreeterOuterClass.HelloRequest helloRequest =   GreeterOuterClass.HelloRequest.newBuilder().setName(name).build();
        return stub.sayHello(helloRequest);
    }

}
