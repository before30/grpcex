package cc.before30.home.metric;

import cc.before30.home.metric.json.JacksonUtils;
import cc.before30.home.metric.sender.HttpMetricSender;
import cc.before30.home.metric.sender.MetricSender;
import cc.before30.home.metric.sender.Slf4jMetricSender;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.util.MeterPartition;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MyMetricMeterRegistry
 *
 * @author before30
 * @since 2019-07-10
 */

@Slf4j
public class MyMetricMeterRegistry extends StepMeterRegistry {

    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new NamedThreadFactory("my-metric-meter-publisher");

    private final MyMetricConfig config;
    private final MetricSender metricSender;
    private final String prefix;

    public MyMetricMeterRegistry(MyMetricConfig config, Clock clock) {
        this(config, clock, DEFAULT_THREAD_FACTORY);
    }

    public MyMetricMeterRegistry(MyMetricConfig config, Clock clock, ThreadFactory threadFactory) {
        super(config, clock);
        this.config = config;
        this.prefix = String.format("%s.%s.%s.%s.%s",
                config.prefix(), config.serviceName(), config.roleName(),
                config.clusterName(), config.hostName());

        switch (config.type()) {
            case TEST:
                metricSender = new HttpMetricSender(config, config.url());
                break;
            case PROD:
            case DEV:
            case CONSOLE:
            default:
                metricSender = new Slf4jMetricSender(LoggerFactory.getLogger(MyMetricMeterRegistry.class));
                break;
        }

        start(threadFactory);
    }

    @Override
    protected void publish() {
        for (List<Meter> batch : MeterPartition.partition(this, Math.min(config.batchSize(), 1000))) {
            //send event
            metricSender.sendEvents(batch.stream().flatMap(
                    meter -> meter.match(
                            this::writeGauge,
                            this::writeCounter,
                            this::writeTimer,
                            this::writeSummary,
                            this::writeLongTaskTimer,
                            this::writeTimeGauge,
                            this::writeFunctionCounter,
                            this::writeFunctionTimer,
                            this::writeMeter)
            ));

        }
    }

    private Stream<String> writeLongTaskTimer(LongTaskTimer ltt) {
        return Stream.of(
                event(
                        MetricRecord.of(prefix, getBaseName(ltt.getId()), "activeTasks", String.valueOf(ltt.activeTasks())),
                        MetricRecord.of(prefix, getBaseName(ltt.getId()), "duration", String.valueOf(ltt.duration(getBaseTimeUnit()))))
        );
    }

    private Stream<String> writeFunctionCounter(FunctionCounter counter) {
        double count = counter.count();
        if (Double.isFinite(count)) {
            return Stream.of(
                    event(MetricRecord.of(prefix, getBaseName(counter.getId()), "throughput", String.valueOf(count))));
        }
        return Stream.empty();
    }

    private Stream<String> writeCounter(Counter counter) {
        return Stream.of(event(
                MetricRecord.of(prefix, getBaseName(counter.getId()), "throughput", String.valueOf(counter.count()))));
    }

    private Stream<String> writeGauge(Gauge gauge) {
        Double value = gauge.value();
        if (Double.isFinite(value)) {
            return Stream.of(event(
                    MetricRecord.of(prefix, getBaseName(gauge.getId()), "value", String.valueOf(value))
            ));
        }
        return Stream.empty();
    }

    private Stream<String> writeTimeGauge(TimeGauge gauge) {
        Double value = gauge.value(getBaseTimeUnit());
        if (Double.isFinite(value)) {
            return Stream.of(event(
                    MetricRecord.of(prefix, getBaseName(gauge.getId()), "value", String.valueOf(value))
            ));
        }
        return Stream.empty();
    }

    private Stream<String> writeSummary(DistributionSummary summary) {
        return Stream.of(
                event(
                        MetricRecord.of(prefix, getBaseName(summary.getId()), "count", String.valueOf(summary.count())),
                        MetricRecord.of(prefix, getBaseName(summary.getId()), "avg", String.valueOf(summary.mean())),
                        MetricRecord.of(prefix, getBaseName(summary.getId()), "total", String.valueOf(summary.totalAmount())),
                        MetricRecord.of(prefix, getBaseName(summary.getId()), "max", String.valueOf(summary.max()))
                )
        );
    }

    private Stream<String> writeTimer(Timer timer) {
        return Stream.of(event(
                MetricRecord.of(prefix, getBaseName(timer.getId()), "count", String.valueOf(timer.count())),
                MetricRecord.of(prefix, getBaseName(timer.getId()), "avg", String.valueOf(timer.mean(getBaseTimeUnit()))),
                MetricRecord.of(prefix, getBaseName(timer.getId()), "totalTime", String.valueOf(timer.totalTime(getBaseTimeUnit()))),
                MetricRecord.of(prefix, getBaseName(timer.getId()), "max", String.valueOf(timer.max(getBaseTimeUnit())))
        ));
    }

    private Stream<String> writeFunctionTimer(FunctionTimer timer) {
        return Stream.of(
                event(
                        MetricRecord.of(prefix, getBaseName(timer.getId()), "count", String.valueOf(timer.count())),
                        MetricRecord.of(prefix, getBaseName(timer.getId()), "avg", String.valueOf(timer.mean(getBaseTimeUnit()))),
                        MetricRecord.of(prefix, getBaseName(timer.getId()), "totalTime", String.valueOf(timer.totalTime(getBaseTimeUnit())))
                )
        );
    }

    private Stream<String> writeMeter(Meter meter) {
        List<MetricRecord> attributes = new ArrayList<>();
        for (Measurement measurement : meter.measure()) {
            double value = measurement.getValue();
            if (!Double.isFinite(value)) {
                continue;
            }
            attributes.add(MetricRecord.of(prefix, getBaseName(meter.getId()),
                    measurement.getStatistic().getTagValueRepresentation(), String.valueOf(value)));
        }

        if (attributes.isEmpty()) {
            return Stream.empty();
        }

        return Stream.of(event(
                attributes.toArray(new MetricRecord[0])));
    }

    private String event(MetricRecord... records) {
        return Arrays.stream(records)
                .map(JacksonUtils::writeAsString)
                .collect(Collectors.joining(","));
    }

    private static final Pattern PATTERN_TAG_BLACKLISTED_CHARS = Pattern.compile("[{}(),=\\[\\]/ ?:.]");

    private String getBaseName(Meter.Id id) {
        String name = id.getName();
        List<Tag> tags = id.getTags();
        if (tags.size() > 0) {
            name = name + "." + sanitizeTag(tags.get(0).getValue());
        }

        return name;
    }

    private String sanitizeTag(String delegated) {
        return PATTERN_TAG_BLACKLISTED_CHARS.matcher(delegated).replaceAll("");
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }

}
