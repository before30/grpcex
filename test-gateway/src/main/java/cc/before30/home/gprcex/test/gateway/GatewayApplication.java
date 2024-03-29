package cc.before30.home.gprcex.test.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * GatewayApplication
 *
 * @author before30
 * @since 2019-07-07
 */

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p ->
                        p.path("/service/**")
                            .filters(f -> f.stripPrefix(1))
                            .uri("lb://micro-service")
                ).build();
    }
}
