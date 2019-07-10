package cc.before30.home.metric.sender;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.lang.NonNull;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * MetricSender
 *
 * @author before30
 * @since 2019-07-10
 */
public interface MetricSender extends Closeable {

    /**
     * Connect to target server to send metric record
     */
    default void connect() {
        // Nothing to do.
    }

    /**
     * Write metric information
     *
     * @param events
     */
    void sendEvents(Stream<String> events);

    @Override
    void close();
}
