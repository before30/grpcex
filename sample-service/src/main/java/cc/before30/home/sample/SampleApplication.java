package cc.before30.home.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SampleApplication
 *
 * @author before30
 * @since 2019-06-23
 */

@SpringBootApplication
@Slf4j
public class SampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION.");
        SpringApplication.run(SampleApplication.class, args);
        log.info("APPLICATION FINISHED.");

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
