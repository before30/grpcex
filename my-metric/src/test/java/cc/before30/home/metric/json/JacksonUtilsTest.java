package cc.before30.home.metric.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * JacksonUtilsTest
 *
 * @author before30
 * @since 2019-07-11
 */
@Slf4j
class JacksonUtilsTest {

    @Test
    void mapperTst() {
        Attribute attribute = new Attribute();
        attribute.setName("Joseph");
        attribute.setAge(28);
        String src = JacksonUtils.writeAsString(attribute);
        Attribute attr = JacksonUtils.readFrom(src, Attribute.class);

        log.info("{}", attr);
        Assertions.assertTrue(attribute.equals(attr));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonDeserialize
    static class Attribute {
        private String name;
        private int age;

        @Override
        public boolean equals(Object that) {
            if (!(that instanceof Attribute)) {
                return false;
            }
            Attribute it = (Attribute)that;
            return this.name.equals(it.name) && this.age == it.age;
        }
    }
}