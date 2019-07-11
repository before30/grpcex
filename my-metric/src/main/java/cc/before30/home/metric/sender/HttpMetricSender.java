package cc.before30.home.metric.sender;

import cc.before30.home.metric.MyMetricConfig;
import io.micrometer.core.ipc.http.HttpSender;
import io.micrometer.core.ipc.http.HttpUrlConnectionSender;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * HttpMetricSender
 *
 * @author before30
 * @since 2019-07-10
 */

@Slf4j
public class HttpMetricSender implements MetricSender {

    private final HttpSender httpSender;
    private final String url;
    private final MyMetricConfig config;

    public HttpMetricSender(MyMetricConfig config, String url) {
        this.config = config;
        this.url = url;
        httpSender = new HttpUrlConnectionSender();
    }

    @Override
    public void sendEvents(Stream<String> events) {
        AtomicInteger totalEvents = new AtomicInteger();
        try {
            httpSender.post(url)
                    .withJsonContent(events.peek(ev -> totalEvents.incrementAndGet()).collect(Collectors.joining(",","[","]")))
                    .send()
                    .onSuccess(resp -> log.debug("successfully sent {} metrics to Metric System.", totalEvents))
                    .onError(resp -> log.error("failed to send metrics to metric system. http {} {}", resp.code(), resp.body()));
        } catch (Throwable e) {
            log.warn("failed to send metrics to metric system", e);
        }
    }
}
