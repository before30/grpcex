package cc.before30.home.grpcex.client.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import com.google.common.util.concurrent.ListenableFuture;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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
    private final List<GreeterGrpc.GreeterFutureStub> stubs;
    private final EurekaClient eurekaClient;


    public GreeterService(Executor executor, Channel channel, @Qualifier("eurekaClient") EurekaClient eurekaClient) {
        this.executor = executor;
        this.stub = GreeterGrpc.newFutureStub(channel);
        this.eurekaClient = eurekaClient;
        Application application = eurekaClient.getApplication("grpc_server");

        List<Integer> ports = application.getInstances().stream().map(instanceInfo -> {
            String s = instanceInfo.getMetadata().get("grpc.port");
            return Integer.parseInt(s);
        }).collect(Collectors.toList());
        List<ManagedChannel> channels = new ArrayList<>();
        for(int port : ports) {
            channels.add(ManagedChannelBuilder.forAddress("127.0.0.1", port)
                    .usePlaintext()
                    .build());
        }
        List<GreeterGrpc.GreeterFutureStub> stubs = new ArrayList<>();
        for (Channel c : channels) {
            stubs.add(GreeterGrpc.newFutureStub(c));
        }
        this.stubs = stubs;
    }

    public ListenableFuture<GreeterOuterClass.HelloReply> greet(String name) {
        final GreeterOuterClass.HelloRequest helloRequest = GreeterOuterClass.HelloRequest.newBuilder().setName(name).build();
        return stub.sayHello(helloRequest);
    }

    public void greets(String name) {
        stubs.stream().forEach(s -> s.sayHello(GreeterOuterClass.HelloRequest.newBuilder().setName(name).build()));
    }

}
