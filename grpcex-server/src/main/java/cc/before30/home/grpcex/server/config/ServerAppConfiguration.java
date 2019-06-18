package cc.before30.home.grpcex.server.config;

import cc.before30.home.grpcex.server.service.GreeterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * ServerAppConfiguration
 *
 * @author before30
 * @since 2019-06-19
 */
@Configuration
public class ServerAppConfiguration {

    @Bean
    public Executor executor() {
        return ForkJoinPool.commonPool();
    }

//    @Bean
//    public GreeterService greeterService() {
//        return new GreeterService();
//    }
}
