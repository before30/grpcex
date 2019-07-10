package cc.before30.home.metric;

import cc.before30.home.metric.sender.MetricSender;
import cc.before30.home.metric.sender.Slf4jMetricSender;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.config.NamingConvention;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.util.DoubleFormat;
import io.micrometer.core.instrument.util.MeterPartition;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.micrometer.core.instrument.util.StringEscapeUtils.escapeJson;

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

    public MyMetricMeterRegistry(MyMetricConfig config, Clock clock) {
        this(config, clock, DEFAULT_THREAD_FACTORY);
    }

    public MyMetricMeterRegistry(MyMetricConfig config, Clock clock, ThreadFactory threadFactory) {
        super(config, clock);
        config().namingConvention(new MyMetricNamingConvention());
        this.config = config;

        start(threadFactory);
    }

    @Override
    protected void publish() {
        for (List<Meter> batch : MeterPartition.partition(this, Math.min(config.batchSize(), 1000))) {
            //send event
            getMyMetricSender(config).sendEvents(batch.stream().flatMap(
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
                event(ltt.getId(),
                        new Attribute("activeTasks", ltt.activeTasks()),
                        new Attribute("duration", ltt.duration(getBaseTimeUnit())))
        );
    }

    // VisibleForTesting
    Stream<String> writeFunctionCounter(FunctionCounter counter) {
        double count = counter.count();
        if (Double.isFinite(count)) {
            return Stream.of(event(counter.getId(), new Attribute("throughput", count)));
        }
        return Stream.empty();
    }

    private Stream<String> writeCounter(Counter counter) {
        return Stream.of(event(counter.getId(), new Attribute("throughput", counter.count())));
    }

    // VisibleForTesting
    Stream<String> writeGauge(Gauge gauge) {
        Double value = gauge.value();
        if (Double.isFinite(value)) {
            return Stream.of(event(gauge.getId(), new Attribute("value", value)));
        }
        return Stream.empty();
    }

    // VisibleForTesting
    Stream<String> writeTimeGauge(TimeGauge gauge) {
        Double value = gauge.value(getBaseTimeUnit());
        if (Double.isFinite(value)) {
            return Stream.of(event(gauge.getId(), new Attribute("value", value)));
        }
        return Stream.empty();
    }

    private Stream<String> writeSummary(DistributionSummary summary) {
        log.info("summary {}", summary.takeSnapshot().percentileValues());
        return Stream.of(
                event(summary.getId(),
                        new Attribute("count", summary.count()),
                        new Attribute("avg", summary.mean()),
                        new Attribute("total", summary.totalAmount()),
                        new Attribute("max", summary.max())
                )
        );
    }

    private Stream<String> writeTimer(Timer timer) {
        log.info("histogram {}", timer.histogramCountAtValue(95));
        log.info("histogram {}", timer.histogramCountAtValue(50));
        log.info("timer snapshot {}", timer.takeSnapshot().percentileValues().length);
        return Stream.of(event(timer.getId(),
                new Attribute("count", timer.count()),
                new Attribute("avg", timer.mean(getBaseTimeUnit())),
                new Attribute("totalTime", timer.totalTime(getBaseTimeUnit())),
                new Attribute("max", timer.max(getBaseTimeUnit()))

        ));

    }

    private Stream<String> writeFunctionTimer(FunctionTimer timer) {
        return Stream.of(
                event(timer.getId(),
                        new Attribute("count", timer.count()),
                        new Attribute("avg", timer.mean(getBaseTimeUnit())),
                        new Attribute("totalTime", timer.totalTime(getBaseTimeUnit()))
                )
        );
    }

    // VisibleForTesting
    Stream<String> writeMeter(Meter meter) {
        // Snapshot values should be used throughout this method as there are chances for values to be changed in-between.
        List<Attribute> attributes = new ArrayList<>();
        for (Measurement measurement : meter.measure()) {
            double value = measurement.getValue();
            if (!Double.isFinite(value)) {
                continue;
            }
            attributes.add(new Attribute(measurement.getStatistic().getTagValueRepresentation(), value));
        }
        if (attributes.isEmpty()) {
            return Stream.empty();
        }
        return Stream.of(event(meter.getId(), attributes.toArray(new Attribute[0])));
    }

    private String event(Meter.Id id, Attribute... attributes) {
        return event(id, Tags.empty(), attributes);
    }

    private String event(Meter.Id id, Iterable<Tag> extraTags, Attribute... attributes) {
        StringBuilder tagsJson = new StringBuilder();

        for (Tag tag : getConventionTags(id)) {
            tagsJson.append(",\"").append(escapeJson(tag.getKey())).append("\":\"").append(escapeJson(tag.getValue())).append("\"");
        }

        NamingConvention convention = config().namingConvention();
        for (Tag tag : extraTags) {
            tagsJson.append(",\"").append(escapeJson(convention.tagKey(tag.getKey())))
                    .append("\":\"").append(escapeJson(convention.tagValue(tag.getValue()))).append("\"");
        }

        return Arrays.stream(attributes)
                .map(attr -> ",\"" + attr.getName() + "\":" + DoubleFormat.decimalOrWhole(attr.getValue().doubleValue()))
                .collect(Collectors.joining("", "{\"eventType\":\"" + escapeJson(getConventionName(id)) + "\"", tagsJson + "}"));
    }


    private static MetricSender getMyMetricSender(MyMetricConfig config) {
        switch (config.type()) {
            case PROD:
            case DEV:
            case CONSOLE:
            default:
                return new Slf4jMetricSender(LoggerFactory.getLogger(MyMetricMeterRegistry.class));
        }
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.SECONDS;
    }

    private class Attribute {
        private final String name;
        private final Number value;

        private Attribute(String name, Number value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Number getValue() {
            return value;
        }
    }

}
