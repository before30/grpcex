package cc.before30.home.grpcex.test.server.config;

import cc.before30.home.metric.MyMetricConfig;
import cc.before30.home.metric.MyMetricType;

import java.time.Duration;

/**
 * TestServerMetricConfig
 *
 * @author before30
 * @since 2019-07-11
 */
public class TestServerMetricConfig implements MyMetricConfig {
    @Override
    public MyMetricType type() {
        return MyMetricType.CONSOLE;
    }

    @Override
    public String roleName() {
        return "myrole";
    }

    @Override
    public String serviceName() {
        return "myservice";
    }

    @Override
    public String clusterName() {
        return null;
    }

    @Override
    public String hostName() {
        return null;
    }

    @Override
    public String prefix() {
        return "host";
    }

    @Override
    public String url() {
        return null;
    }

    @Override
    public String get(String key) {
        return null;
    }


    @Override
    public Duration step() {
        return Duration.ofSeconds(30);
    }
}
