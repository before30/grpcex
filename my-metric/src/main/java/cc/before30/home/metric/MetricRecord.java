package cc.before30.home.metric;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * MetricRecord
 *
 * @author before30
 * @since 2019-07-11
 */
@Getter
@Setter
@EqualsAndHashCode
public class MetricRecord {

    private static final long serialVersionUID = -2381826104731011710L;
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");
    private static final String DASH = "-";
    static String sanitize(@Nonnull final String string) {
        return WHITESPACE.matcher(string.trim()).replaceAll(DASH);
    }

    public static long currentUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    @Nonnull
    public static MetricRecord of(
            @Nonnull final String prefix,
            @Nonnull final String id,
            @Nonnull final String key,
            @Nonnull final String value) {
        return of(prefix, id, key, value, currentUnixTimestamp());
    }

    @Nonnull
    public static MetricRecord of(
            @Nonnull final String prefix,
            @Nonnull final String id,
            @Nonnull final String key,
            @Nonnull final String value,
            long unixTimestamp) {
        return new MetricRecord(prefix, id, key, value, unixTimestamp);
    }

    @Nonnull
    private String path;

    @Nonnull
    private String value;

    /**
     * It's Unix Timestamp (unit is Seconds not Milliseconds)
     */
    private long timestamp;

    private MetricRecord() { }

    public MetricRecord(@NonNull final String prefix,
                        @Nonnull final String id,
                        @Nonnull final String key,
                        @Nonnull final String value) {
        this(prefix, id, key, value, currentUnixTimestamp());
    }

    public MetricRecord(@Nonnull final String prefix,
                        @Nonnull final String id,
                        @Nonnull final String key,
                        @Nonnull final String value,
                        long unixTimestamp) {
        this.path = sanitize(prefix + "." + id + "." + key);
        this.value = sanitize(value);
        this.timestamp = unixTimestamp;
    }

    @Override
    public String toString() {
        return path + ":" + value + ":" + timestamp;
    }
}
