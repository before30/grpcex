package cc.before30.home.metric.dropwizard.sender;

/**
 * MetricFormatter
 *
 * @author before30
 * @since 2019-07-16
 */
public class MetricFormatter {
    private static final String METRIC_FORMAT =
            "{\"path\":\"%s\", \"value\":\"%s\", \"timestamp\":\"%s\"}";

    public static String formatMetric(String path, String value, long time) {
        return String.format(METRIC_FORMAT, path, value, String.valueOf(time));
    }
}
