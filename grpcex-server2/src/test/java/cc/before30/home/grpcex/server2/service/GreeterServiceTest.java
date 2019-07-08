package cc.before30.home.grpcex.server2.service;

import cc.before30.home.grpc.proto.GreeterOuterClass;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * GreeterServiceTest
 *
 * @author before30
 * @since 2019-07-08
 */
public class GreeterServiceTest {

    @Test
    public void reatorTest() {
        Mono<String> joel = Mono.just("Joel");
        joel
                .flux()
                .flatMap(name -> Flux.just("Hello", "안녕", "Hola").map(
                        str -> str + " " + name
                ))
                .subscribe(greeting -> System.out.println(greeting));
    }
}
