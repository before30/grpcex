package cc.before30.home.metric;

import io.micrometer.core.instrument.step.StepRegistryConfig;

/**
 * MyMetricConfig
 *
 * @author before30
 * @since 2019-07-10
 */
public interface MyMetricConfig extends StepRegistryConfig {
    MyMetricType type();

    String roleName();

    String serviceName();

    String instanceId();

    String prefix();
}
