package cc.before30.home.grpcex.test.server.io;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * IoTest
 *
 * @author before30
 * @since 2019-07-07
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8080"})
public class IoTest {
    private static final String ONE_SECOND_URL = "http://localhost:8080/1second";
    private static final int LOOP_COUNT = 100;

    private final WebClient webClient = WebClient.create();
    private final CountDownLatch count = new CountDownLatch(LOOP_COUNT);

    @BeforeAll
    public static void initAll() {
        System.setProperty("reactor.netty.ioWorkerCount", "1");
    }

    @Test
    public void blocking() {
        final RestTemplate restTemplate = new RestTemplate();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 3; i++) {
            final ResponseEntity<String> response =
                    restTemplate.exchange(ONE_SECOND_URL, HttpMethod.GET, HttpEntity.EMPTY, String.class);
            assertThat(response.getBody()).contains("success");
        }

        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeSeconds());
    }

    @Test
    public void nonBlocking1() throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        this.webClient
                .get()
                .uri(ONE_SECOND_URL)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(System.out::println)
                .subscribe();

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

        Thread.sleep(5_000);
    }

    @Test
    public void thread() {
        System.out.println(1);

        new Thread(() -> System.out.println(2)).start();

        System.out.println(3);
    }

    @Test
    public void nonBlocking2()  throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        this.webClient
                .get()
                .uri(ONE_SECOND_URL)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .subscribe(it -> {
                    stopWatch.stop();
                    System.out.println(stopWatch.getTotalTimeMillis());
                });

        Thread.sleep(5000);
    }

    @Test
    public void nonBlocking3() throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.setProperty("reactor.netty.ioWorkerCount", "10");
        WebClient webClient2 = WebClient.create();

        for (int i = 0; i < LOOP_COUNT; i++) {
            webClient2
                    .get()
                    .uri(ONE_SECOND_URL)
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .subscribe(it -> {
                        count.countDown();
                        System.out.println(it);
                    });
        }

        count.await(10, TimeUnit.SECONDS);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
