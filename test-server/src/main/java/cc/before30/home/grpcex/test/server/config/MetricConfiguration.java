package cc.before30.home.grpcex.test.server.config;

import cc.before30.home.metric.MyMetricMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MetricConfiguration
 *
 * @author before30
 * @since 2019-07-11
 */

@Configuration
public class MetricConfiguration {

    @Bean
    public MyMetricMeterRegistry myMetricMeterRegistry() {

        return new MyMetricMeterRegistry(new TestServerMetricConfig(), Clock.SYSTEM);
    }
}
