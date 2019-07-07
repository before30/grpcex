package cc.before30.home.grpcex.test.microservice;

import cc.before30.home.grpcex.test.microservice.config.AppConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ReactiveValueOperations;

/**
 * MicroserviceApplication
 *
 * @author before30
 * @since 2019-07-07
 */

@SpringBootApplication
public class MicroserviceApplication implements CommandLineRunner {

    private final ReactiveValueOperations reactiveValueOperations;

    public MicroserviceApplication(@Lazy ReactiveValueOperations reactiveValueOperations) {
        this.reactiveValueOperations = reactiveValueOperations;
    }

    @Override
    public void run(String... args) throws Exception {
        this.reactiveValueOperations.set(AppConfiguration.EVENT_APPLY_KEY, "0").subscribe();
    }
}
