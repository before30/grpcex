package cc.before30.home.grpcex.server2.service;

import cc.before30.home.grpc.proto.GreeterOuterClass;
import cc.before30.home.grpc.proto.ReactorGreeterGrpc;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ReactiveGreeterService
 *
 * @author before30
 * @since 2019-07-08
 */

@Slf4j
public class ReactiveGreeterService extends ReactorGreeterGrpc.GreeterImplBase {

    @Override
    public Mono<GreeterOuterClass.HelloReply> sayHello(Mono<GreeterOuterClass.HelloRequest> request) {
        return request
                .map(GreeterOuterClass.HelloRequest::getName)
                .map(name -> "Hello " + name)
                .map(greeting -> GreeterOuterClass.HelloReply.newBuilder().setMessage(greeting).build());
    }

    @Override
    public Flux<GreeterOuterClass.HelloReply> multiSayHello(Mono<GreeterOuterClass.HelloRequest> request) {
        return request
                .map(GreeterOuterClass.HelloRequest::getName)
                .flux()
                .flatMap(name -> Flux.just("Hello", "안녕", "Hola").map(
                        str -> str + " " + name
                ))
                .map(greeting  -> GreeterOuterClass.HelloReply.newBuilder().setMessage(greeting).build());
    }

    @Override
    public Flux<GreeterOuterClass.HelloReply> streamSayHello(Flux<GreeterOuterClass.HelloRequest> request) {
        return request
                .map(GreeterOuterClass.HelloRequest::getName)
                .map(name -> "Hello " + name)
                .map(greeting -> GreeterOuterClass.HelloReply.newBuilder().setMessage(greeting).build());
    }
}
