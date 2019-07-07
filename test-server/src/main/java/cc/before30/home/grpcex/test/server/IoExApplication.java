package cc.before30.home.grpcex.test.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class IoExApplication {

    private final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) {
        SpringApplication.run(IoExApplication.class, args);
    }

    @GetMapping("/1second")
    public String oneSecond() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "success - " + this.counter.incrementAndGet();
    }
}
