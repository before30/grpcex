package cc.before30.home.grpcex.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * RedisApplication
 *
 * @author before30
 * @since 2019-06-19
 */

@SpringBootApplication
@EnableEurekaClient
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
