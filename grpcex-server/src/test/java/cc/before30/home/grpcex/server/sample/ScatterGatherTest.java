package cc.before30.home.grpcex.server.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import rx.Observable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ScatterGatherTest
 *
 * @author before30
 * @since 2019-07-03
 */

@Slf4j
public class ScatterGatherTest {

    @Test
    public void testScatterGather() {
        ExecutorService executors = Executors.newFixedThreadPool(5);
        List<Flux<String>> fluxList = IntStream.range(0, 10)
                .boxed()
                .map(i -> generateTask(executors, i)).collect(Collectors.toList());

        Mono<List<String>> merged = Flux.merge(fluxList).collectList();

        List<String> list = merged.block();

        log.info(list.toString());


    }

    public Flux<String> generateTask(ExecutorService executorService, int i) {
        return Flux.<String>create(s -> {
            long delay = RandomUtils.nextInt(100, 200);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                }
                s.next(i + "-test");
                s.complete();
        })
                .log()
                .subscribeOn(Schedulers.fromExecutor(executorService))
                .timeout(Duration.ofMillis(150))
                .doOnError(e -> log.error(e.getMessage(), e))
                .onErrorReturn("error");
    }

    @Test
    public void testScatterGatherWithRx() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Observable<String>> obs =
                IntStream.range(0, 10)
                .boxed()
                .map(i -> generateTask(i, executorService)).collect(Collectors.toList());

        Observable<List<String>> merged = Observable.merge(obs).toList();
        List<String> result = merged.toBlocking().first();
        log.info(result.toString());
    }

    private Observable<String> generateTask(int i, ExecutorService executorService) {
        return Observable
                .<String>create(s -> {
                            s.onNext(i + "-test");
                            s.onCompleted();
                        }).subscribeOn(rx.schedulers.Schedulers.from(executorService));

    }
}
