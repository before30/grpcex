package cc.before30.home.grpcex.server2.service;

import cc.before30.home.grpc.proto.GreeterGrpc;
import cc.before30.home.grpc.proto.GreeterOuterClass;
import io.grpc.stub.StreamObserver;

/**
 * GreeterService
 *
 * @author before30
 * @since 2019-07-08
 */
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String name = request.getName();
        responseObserver.onNext(GreeterOuterClass.HelloReply.newBuilder().setMessage(name).build());
        responseObserver.onCompleted();
    }

    @Override
    public void multiSayHello(GreeterOuterClass.HelloRequest request, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        String name = request.getName();
        responseObserver.onNext(GreeterOuterClass.HelloReply.newBuilder().setMessage("Welcome " + name).build());
        responseObserver.onNext(GreeterOuterClass.HelloReply.newBuilder().setMessage("안녕 " + name).build());
        responseObserver.onNext(GreeterOuterClass.HelloReply.newBuilder().setMessage("Hola " + name).build());
        responseObserver.onNext(GreeterOuterClass.HelloReply.newBuilder().setMessage("Bonjour " + name).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreeterOuterClass.HelloRequest> streamSayHello(StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        return new StreamObserver<GreeterOuterClass.HelloRequest>() {
            @Override
            public void onNext(GreeterOuterClass.HelloRequest request) {
                String name = request.getName();
                responseObserver.onNext(GreeterOuterClass.HelloReply.newBuilder().setMessage("Welcome " + name).build());
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
