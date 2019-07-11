package cc.before30.home.metric.sender;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Slf4jMetricSender
 *
 * @author before30
 * @since 2019-07-10
 */
public class Slf4jMetricSender implements MetricSender {

    @Nonnull
    private final Logger logger;

    public Slf4jMetricSender(@Nonnull final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void sendEvents(Stream<String> events) {
        logger.info("start to send metrics data...");
        events.forEach(it -> logger.info("{}", it));
    }

}
