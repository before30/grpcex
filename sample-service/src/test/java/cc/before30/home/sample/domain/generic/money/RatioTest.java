package cc.before30.home.sample.domain.generic.money;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RatioTest
 *
 * @author before30
 * @since 2019-06-23
 */
class RatioTest {

    @Test
    public void money_percent() {
        Ratio tenPercent = Ratio.valueOf(0.1);
        Money thousandWon = Money.wons(1_000);

        assertEquals(tenPercent.of(thousandWon), Money.wons(100));
//        Assert.assertThat(tenPercent.of(thousandWon), is(Money.wons(100)));
    }
}