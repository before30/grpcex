package cc.before30.home.grpcex.client2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * GrpcClient2Application
 *
 * @author before30
 * @since 2019-07-09
 */

@SpringBootApplication
public class GrpcClient2Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GrpcClient2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
