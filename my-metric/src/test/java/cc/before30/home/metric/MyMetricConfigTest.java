package cc.before30.home.metric;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MyMetricConfigTest
 *
 * @author before30
 * @since 2019-07-11
 */
class MyMetricConfigTest {

    @Test
    void returnType() {
        Assertions.assertThat(new TestMyMetricConfig().type()).isEqualTo(MyMetricType.CONSOLE);
    }

    @Test
    void returnRoleName() {
        Assertions.assertThat(new TestMyMetricConfig().roleName()).isEqualTo("roleName");
    }

    @Test
    void returnServiceName() {
        Assertions.assertThat(new TestMyMetricConfig().serviceName()).isEqualTo("serviceName");
    }

    @Test
    void returnClusterName() {
        Assertions.assertThat(new TestMyMetricConfig().clusterName()).isEqualTo("clusterName");
    }

    @Test
    void returnHostName() {
        Assertions.assertThat(new TestMyMetricConfig().hostName()).isEqualTo("hostName");
    }

    @Test
    void returnPrefix() {
        Assertions.assertThat(new TestMyMetricConfig().prefix()).isEqualTo("prefix");
    }

    private static class TestMyMetricConfig implements MyMetricConfig {

        @Override
        public MyMetricType type() {
            return MyMetricType.CONSOLE;
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
            return null;
        }

        @Override
        public String get(String key) {
            return null;
        }
    }
}