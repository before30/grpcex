package cc.before30.home.grpcex.client2.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import io.grpc.ManagedChannel;

import javax.annotation.Nonnull;
import java.nio.channels.Channel;

/**
 * RxGreeterGrpcStubRepository
 *
 * @author before30
 * @since 2019-07-09
 */
public class RxGreeterGrpcStubRepository implements GrpcStubRepository {

    private final ManagedChannel channel;
    private GreeterGrpc.GreeterBlockingStub blockingStub;
    private GreeterGrpc.GreeterStub stub;
    private GreeterGrpc.GreeterFutureStub futureStub;

    public RxGreeterGrpcStubRepository(@Nonnull ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
        this.stub = GreeterGrpc.newStub(channel);
        this.futureStub = GreeterGrpc.newFutureStub(channel);
    }

}
