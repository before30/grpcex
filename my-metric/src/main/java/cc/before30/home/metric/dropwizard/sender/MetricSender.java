package cc.before30.home.metric.dropwizard.sender;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * MetricSender
 *
 * @author before30
 * @since 2019-07-16
 */
public interface MetricSender extends Closeable {

    void connect();
    void send(@Nonnull final List<String> records);

    @Override
    void close();
}
