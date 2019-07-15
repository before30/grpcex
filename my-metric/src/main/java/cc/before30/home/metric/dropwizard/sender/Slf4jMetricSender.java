package cc.before30.home.metric.dropwizard.sender;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Slf4jMetricSender
 *
 * @author before30
 * @since 2019-07-16
 */
@Slf4j
public class Slf4jMetricSender implements MetricSender {
    @Override
    public void connect() {

    }

    @Override
    public void send(@Nonnull List<String> records) {

        for (String record: records) {
            log.info(record);
        }

    }

    @Override
    public void close() {

    }
}
