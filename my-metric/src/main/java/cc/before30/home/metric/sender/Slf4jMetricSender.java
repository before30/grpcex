package cc.before30.home.metric.sender;

import io.micrometer.core.lang.NonNull;
import org.slf4j.Logger;

import java.util.stream.Stream;

/**
 * Slf4jMetricSender
 *
 * @author before30
 * @since 2019-07-10
 */
public class Slf4jMetricSender implements MetricSender {

    @NonNull
    private final Logger logger;

    public Slf4jMetricSender(@NonNull final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void connect() {
        logger.info("slf4j metric sender connected...");
    }

    @Override
    public void sendEvents(Stream<String> events) {
        logger.info("start to send metrics data...");
        events.forEach(it -> logger.info("{}", it));
    }

    @Override
    public void close() {
        logger.info("slf4j metric sender closed...");
    }
}
