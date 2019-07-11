package cc.before30.home.metric.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;

/**
 * JacksonUtils
 *
 * @author before30
 * @since 2019-07-11
 */

@Slf4j
@UtilityClass
public class JacksonUtils {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Nonnull
    public static String writeAsString(Object payload) {
        if (Objects.isNull(payload)) {
            return StringUtils.EMPTY;
        }

        try {
            return mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.warn("Fail to serialize payload. payload={}", payload, e);
            return StringUtils.EMPTY;
        }
    }

    @Nullable
    public static <T> T readFrom(String src, Class<T> valueType) {
        if (StringUtils.isEmpty(src)) {
            return null;
        }

        try {
            return mapper.readValue(src, valueType);
        } catch (IOException e) {
            log.warn("Fail to read src. valueType={}", valueType, e);
            return null;
        }
    }
}
