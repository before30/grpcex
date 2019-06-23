package cc.before30.home.sample.domain.generic.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateTimePeriodTest
 *
 * @author before30
 * @since 2019-06-23
 */
class DateTimePeriodTest {

    @Test
    public void time_period_test() {
        LocalDateTime when = LocalDateTime.of(2019, 1, 1, 10, 30);

        assertTrue(DateTimePeriod.between(when, when.plusMinutes(1)).contains(when));
        assertTrue(DateTimePeriod.between(when.minusMinutes(1), when).contains(when));
        assertFalse(DateTimePeriod.between(when.plusMinutes(1), when.plusMinutes(2)).contains(when));
        assertFalse(DateTimePeriod.between(when.minusMinutes(2), when.minusMinutes(1)).contains(when));
    }

}