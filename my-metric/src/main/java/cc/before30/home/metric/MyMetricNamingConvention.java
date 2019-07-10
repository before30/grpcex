package cc.before30.home.metric;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.NamingConvention;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import io.micrometer.core.lang.Nullable;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * MyMetricNamingConvention
 *
 * @author before30
 * @since 2019-07-10
 */
public class MyMetricNamingConvention implements NamingConvention {

    private final NamingConvention delegate;

    private static final Pattern PATTERN_NAME_BLACKLISTED_CHARS = Pattern.compile("[{}(),=\\[\\]/ ?:]");
    private static final Pattern PATTERN_TAG_BLACKLISTED_CHARS = Pattern.compile("[{}(),=\\[\\]/ ?:.]");

    public MyMetricNamingConvention() {
        this(NamingConvention.camelCase);
    }

    public MyMetricNamingConvention(NamingConvention delegate) {
        this.delegate = delegate;
    }

    @Override
    public String name(String name, Meter.Type type, @Nullable String baseUnit) {
        return sanitizeName(this.delegate.name(normalize(name), type, baseUnit));
    }

    @Override
    public String tagKey(String key) {
        return sanitizeTag(this.delegate.tagKey(normalize(key)));
    }

    @Override
    public String tagValue(String value) {
        return sanitizeTag(this.delegate.tagValue(normalize(value)));
    }

    private String normalize(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFKD);
    }

    private String sanitizeName(String delegated) {
        return PATTERN_NAME_BLACKLISTED_CHARS.matcher(delegated).replaceAll("_");
    }

    private String sanitizeTag(String delegated) {
        return PATTERN_TAG_BLACKLISTED_CHARS.matcher(delegated).replaceAll("_");
    }
}
