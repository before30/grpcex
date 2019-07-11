package cc.before30.home.metric.sender;

import java.util.stream.Stream;

/**
 * MetricSender
 *
 * @author before30
 * @since 2019-07-10
 */
public interface MetricSender {
    /**
     * Write metric information
     *
     * @param events
     */
    void sendEvents(Stream<String> events);

}
