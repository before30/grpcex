package cc.before30.home.metric.dropwizard;

import cc.before30.home.metric.dropwizard.sender.MetricFormatter;
import cc.before30.home.metric.dropwizard.sender.MetricSender;
import com.codahale.metrics.*;
import com.codahale.metrics.Timer;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricAttribute.*;
import static com.codahale.metrics.MetricAttribute.MEAN_RATE;

/**
 * MetricReporter
 *
 * @author before30
 * @since 2019-07-16
 */

@Slf4j
@Getter
@Builder
public class MetricReporter extends ScheduledReporter {

    public static final String REPORTER_NAME = "MY-METRIC-REPORTER";
    public static final String PATH_DELIMITER = ".";

    @Nonnull
    public static MetricReporterBuilder forRegistry(@Nonnull final MetricRegistry registry) {
        return new MetricReporterBuilder();
    }

    private final MetricContext context;
    private final MetricRegistry registry;
    private final MetricSender sender;
    private final Clock clock;


    protected MetricReporter(MetricRegistry registry,
                             MetricSender sender,
                             MetricContext context,
                             Clock clock,
                             String name,
                             MetricFilter filter,
                             TimeUnit rateUnit,
                             TimeUnit durationUnit,
                             ScheduledExecutorService executor,
                             boolean shutdownExecutorOnStop,
                             Set<MetricAttribute> disabledMetricAttributes) {
        super(registry, name, filter, rateUnit, durationUnit,
                executor, shutdownExecutorOnStop, disabledMetricAttributes);

        this.registry = registry;
        this.sender = sender;
        this.clock = clock;
        this.context = context;

        log.info("Create Metric Reporter instance. name={}, sender={}", name, sender);
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges,
                       SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms,
                       SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {

        try {
            log.debug("Start to report metrics...");
            final List<String> records = new ArrayList<>();
            final long startTimestamp = clock.getTime();
            final long unixTimestamp = startTimestamp / 1000L;

            sender.connect();
            reportGauges(records, gauges, unixTimestamp);
            reportCounters(records, counters, unixTimestamp);
            reportHistograms(records, histograms, unixTimestamp);
            reportMeters(records, meters, unixTimestamp);
            reportTimers(records, timers, unixTimestamp);

            if (!records.isEmpty()) {
                sender.send(records);
            }
        } catch (Throwable e) {
            log.error("Fail to report metrics", e);
        } finally {
            sender.close();
        }
    }

    @Override
    public void stop() {
        log.info("Stopping MetricReporter ...");
        try {
            super.stop();
        } finally {
            sender.close();
        }
        log.info("Stopped MetricReporter");
    }

    private void reportGauges(final List<String> records,
                              @Nonnull final SortedMap<String, Gauge> gauges,
                              long unixTimestamp) {
        gauges.forEach((key, gauge) -> {
            // Gauge 는 여러가지 수형을 가질 수 있지만, Number 형식만 가능하도록 필터링합니다.
            String value = format(gauge.getValue());
            if (StringUtils.isNotBlank(value)) {
                records.add(of(key, value, unixTimestamp));
            }
        });
    }

    private void reportCounters(final List<String> records,
                                @Nonnull final SortedMap<String, Counter> counters,
                                long unixTimestamp) {
        counters.forEach((key, counter) ->
                records.add(of(path(key, COUNT), format(counter.getCount()), unixTimestamp)));
    }

    private void reportHistograms(List<String> records,
                                  @Nonnull final SortedMap<String, Histogram> histograms,
                                  long unixTimestamp) {
        for (Map.Entry<String, Histogram> entry : histograms.entrySet()) {
            String key = entry.getKey();
            Histogram histogram = entry.getValue();
            canAdd(records, COUNT, of(path(key, COUNT), format(histogram.getCount()), unixTimestamp));

            Snapshot snapshot = histogram.getSnapshot();

            canAdd(records, MAX, of(path(key, MAX), format(snapshot.getMax()), unixTimestamp));
            canAdd(records, MEAN, of(path(key, MEAN), format(snapshot.getMean()), unixTimestamp));
            canAdd(records, MIN, of(path(key, MIN), format(snapshot.getMin()), unixTimestamp));
            canAdd(records, STDDEV, of(path(key, STDDEV), format(snapshot.getStdDev()), unixTimestamp));
            canAdd(records, P50, of(path(key, P50), format(snapshot.getMedian()), unixTimestamp));
            canAdd(records, P75, of(path(key, P75), format(snapshot.get75thPercentile()), unixTimestamp));
            canAdd(records, P95, of(path(key, P95), format(snapshot.get95thPercentile()), unixTimestamp));
            canAdd(records, P98, of(path(key, P98), format(snapshot.get98thPercentile()), unixTimestamp));
            canAdd(records, P99, of(path(key, P99), format(snapshot.get99thPercentile()), unixTimestamp));
            canAdd(records, P999, of(path(key, P999), format(snapshot.get999thPercentile()), unixTimestamp));
        }
    }

    private void reportMeters(final List<String> records,
                              @Nonnull final SortedMap<String, Meter> meters,
                              long unixTimestamp) {
        meters.forEach((key, meter) -> reportMetered(records, key, meter, unixTimestamp));
    }

    private void reportTimers(List<String> records,
                              SortedMap<String, Timer> timers,
                              long unixTimestamp) {
        timers.forEach((key, timer) -> {
            reportMetered(records, key, timer, unixTimestamp);

            Snapshot snapshot = timer.getSnapshot();

            canAdd(records, MAX, of(path(key, MAX), format(convertDuration(snapshot.getMax())), unixTimestamp));
            canAdd(records, MEAN, of(path(key, MEAN), format(convertDuration(snapshot.getMean())), unixTimestamp));
            canAdd(records, MIN, of(path(key, MIN), format(convertDuration(snapshot.getMin())), unixTimestamp));
            canAdd(records, STDDEV, of(path(key, STDDEV), format(convertDuration(snapshot.getStdDev())), unixTimestamp));
            canAdd(records, P50, of(path(key, P50), format(convertDuration(snapshot.getMedian())), unixTimestamp));
            canAdd(records, P75, of(path(key, P75), format(convertDuration(snapshot.get75thPercentile())), unixTimestamp));
            canAdd(records, P95, of(path(key, P95), format(convertDuration(snapshot.get95thPercentile())), unixTimestamp));
            canAdd(records, P98, of(path(key, P98), format(convertDuration(snapshot.get98thPercentile())), unixTimestamp));
            canAdd(records, P99, of(path(key, P99), format(convertDuration(snapshot.get99thPercentile())), unixTimestamp));
            canAdd(records, P999, of(path(key, P999), format(convertDuration(snapshot.get999thPercentile())), unixTimestamp));
        });
    }

    private void reportMetered(List<String> records,
                               String key, Metered metered,
                               long unixTimestamp) {
        canAdd(records, COUNT, of(path(key, COUNT), format(metered.getCount()), unixTimestamp));
        canAdd(records, M1_RATE, of(path(key, M1_RATE), format(convertRate(metered.getOneMinuteRate())), unixTimestamp));
        canAdd(records, M5_RATE, of(path(key, M5_RATE), format(convertRate(metered.getFiveMinuteRate())), unixTimestamp));
        canAdd(records, M15_RATE, of(path(key, M15_RATE), format(convertRate(metered.getFifteenMinuteRate())), unixTimestamp));
        canAdd(records, MEAN_RATE, of(path(key, MEAN_RATE), format(convertRate(metered.getMeanRate())), unixTimestamp));
    }

    @Nonnull
    private static String path(@Nonnull final String key, @Nonnull final MetricAttribute type) {
        return key + PATH_DELIMITER + type.getCode();
    }

    private String of(String key, String value, long unixTimestamp) {
        String path = context.getPrefix() + PATH_DELIMITER + key;
        return MetricFormatter.formatMetric(path, value, unixTimestamp);
    }

    private void canAdd(List<String> records, MetricAttribute type, String record) {
        if (!getDisabledMetricAttributes().contains(type)) {
            records.add(record);
        }
    }

    @Nullable
    private String format(Object o) {
        if (o instanceof Float) {
            return format(((Float) o).doubleValue());
        } else if (o instanceof Double) {
            return format(((Double) o).doubleValue());
        } else if (o instanceof Byte) {
            return format(((Byte) o).longValue());
        } else if (o instanceof Short) {
            return format(((Short) o).longValue());
        } else if (o instanceof Integer) {
            return format(((Integer) o).longValue());
        } else if (o instanceof Long) {
            return format(((Long) o).longValue());
        } else if (o instanceof BigInteger) {
            return format(((BigInteger) o).doubleValue());
        } else if (o instanceof BigDecimal) {
            return format(((BigDecimal) o).doubleValue());
        } else if (o instanceof Boolean) {
            return format(((Boolean) o) ? 1 : 0);
        }
        return null;
    }

    @Nonnull
    private String format(long n) {
        return Long.toString(n);
    }

    protected String format(double v) {
        // the Carbon plaintext format is pretty underspecified, but it seems like it just wants
        // US-formatted digits
        return String.format(Locale.US, "%2.2f", v);
    }

    public static class Builder {
        private final MetricRegistry registry;
        private Clock clock;
        private MetricFilter filter;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private TimeZone timeZone;
        private ScheduledExecutorService executor;
        private boolean shutdownExecutorOnStop;
        private Set<MetricAttribute> disabledMetricAttributes;

        public Builder(MetricRegistry registry) {
            this.registry = registry;
            this.clock = Clock.defaultClock();
            this.filter = MetricFilter.ALL;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.timeZone = TimeZone.getDefault();
            this.executor = null;
            this.shutdownExecutorOnStop = true;
            this.disabledMetricAttributes = Collections.emptySet();
        }

        public MetricReporter.Builder clock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public MetricReporter.Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        public MetricReporter.Builder rateUnit(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        public MetricReporter.Builder durationUnit(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        public MetricReporter.Builder timeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public MetricReporter.Builder executor(ScheduledExecutorService executor) {
            this.executor = executor;
            return this;
        }

        public MetricReporter.Builder shutdownExecutorOnStop(boolean shutdownExecutorOnStop) {
            this.shutdownExecutorOnStop = shutdownExecutorOnStop;
            return this;
        }

        public MetricReporter.Builder disabledMetricAttributes(Set<MetricAttribute> disabledMetricAttributes) {
            this.disabledMetricAttributes = disabledMetricAttributes;
            return this;
        }

        public MetricReporter build(@Nonnull final MetricSender sender,
                                    @Nonnull final MetricContext context) {
            return new MetricReporter(registry,
                    sender,
                    context,
                    clock,
                    REPORTER_NAME,
                    filter,
                    rateUnit,
                    durationUnit,
                    executor,
                    shutdownExecutorOnStop,
                    disabledMetricAttributes);
        }
    }
}
