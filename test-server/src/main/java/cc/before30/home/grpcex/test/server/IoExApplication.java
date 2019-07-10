package cc.before30.home.grpcex.test.server;

import cc.before30.home.metric.MyMetricMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IoExApplication
 *
 * @author before30
 * @since 2019-07-07
 */
@SpringBootApplication
@RestController
@Slf4j
public class IoExApplication implements CommandLineRunner {

    private final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) {
        SpringApplication.run(IoExApplication.class, args);
    }

    @GetMapping("/1second")
    public String oneSecond() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        myMetricMeterRegistry.counter("test1.counter.test");
        myMetricMeterRegistry.gauge("test2.gauge", 100 + RandomUtils.nextInt(10, 100));
        TimeUnit.SECONDS.sleep(1);
        myMetricMeterRegistry.timer("test3").record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
        myMetricMeterRegistry.summary("test4").record(System.currentTimeMillis() - startTime);
        return "success - " + this.counter.incrementAndGet();
    }

    @Autowired
    MyMetricMeterRegistry myMetricMeterRegistry;

    @Override
    public void run(String... args) throws Exception {
        myMetricMeterRegistry.counter("test1.counter.test3");
        myMetricMeterRegistry.counter("test1.counter.test3");
        myMetricMeterRegistry.counter("test1.counter.test3");

    }
}
