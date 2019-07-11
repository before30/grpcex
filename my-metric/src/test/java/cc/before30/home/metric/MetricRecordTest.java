package cc.before30.home.metric;

import cc.before30.home.metric.json.JacksonUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MetricRecordTest
 *
 * @author before30
 * @since 2019-07-12
 */
class MetricRecordTest {

    @Test
    void metricRecordConvertions() {
        MetricRecord metricRecord = MetricRecord.of("prefix", "id", "key", "value");
        String json = JacksonUtils.writeAsString(metricRecord);

        Assertions.assertThat(json).contains(metricRecord.getPath());
    }

    @Test
    void metricRecordCovertionsII() {
        MetricRecord metricRecord = MetricRecord.of("prefix", "id", "key.kk", "v\".a\"lue");
        String json = JacksonUtils.writeAsString(metricRecord);
        Assertions.assertThat(json).contains("v\\\".a\\\"lue");
    }
}