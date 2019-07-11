package cc.before30.home.metric;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.lanwen.wiremock.ext.WiremockResolver;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MyMetricMeterRegistryTest
 *
 * @author before30
 * @since 2019-07-12
 */

@ExtendWith(WiremockResolver.class)
class MyMetricMeterRegistryTest {

    @Test
    void encodeMetricName(@WiremockResolver.Wiremock WireMockServer server) {
        MyMetricMeterRegistry registry = new MyMetricMeterRegistry(new MyMetricConfig() {
            @Override
            public MyMetricType type() {
                return MyMetricType.TEST;
            }

            @Override
            public String roleName() {
                return "roleName";
            }

            @Override
            public String serviceName() {
                return "serviceName";
            }

            @Override
            public String clusterName() {
                return "clusterName";
            }

            @Override
            public String hostName() {
                return "hostName";
            }

            @Override
            public String prefix() {
                return "prefix";
            }

            @Override
            public String url() {
                return "http://localhost:"+server.port();
            }

            @Override
            public String get(String key) {
                return null;
            }
        }, Clock.SYSTEM);

        server.stubFor(any(anyUrl()));

        Counter.builder("my.counter#abc")
                .baseUnit(TimeUnit.MILLISECONDS.toString().toLowerCase())
                .register(registry)
                .increment(Math.PI);
        registry.publish();

        server.verify(1, postRequestedFor(
                urlMatching("/")));
        registry.close();

    }

}