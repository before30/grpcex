package cc.before30.home.grpcex.client.controller;

import cc.before30.home.grpc.proto.GreeterOuterClass;
import cc.before30.home.grpcex.client.service.GreeterService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * HelloController
 *
 * @author before30
 * @since 2019-06-19
 */

@RestController
public class HelloController {

    private final Executor executor;
    private final GreeterService greeterService;

    public HelloController(GreeterService greeterService,
                           Executor executor) {
        this.greeterService = greeterService;
        this.executor = executor;
    }

    @GetMapping("/hello")
    public String hello() {
        return "world";
    }

    @GetMapping("/greet/{name}")
    public CompletableFuture greet(@PathVariable("name") String name) {
        final CompletableFuture<String> completableFuture = new CompletableFuture();
        Futures.addCallback(greeterService.greet(name), new FutureCallback<GreeterOuterClass.HelloReply>() {
            @Override
            public void onSuccess(@NullableDecl GreeterOuterClass.HelloReply result) {
                completableFuture.complete(result.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                completableFuture.completeExceptionally(t);
            }
        }, executor);

        greeterService.greets(name);
        return completableFuture;
    }
}
