package cc.before30.home.sample.domain.shop;

import cc.before30.home.sample.domain.Fixtures;
import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.generic.money.Ratio;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static cc.before30.home.sample.domain.Fixtures.*;
import static org.hamcrest.core.Is.*;

/**
 * ShopTest
 *
 * @author before30
 * @since 2019-06-23
 */
public class ShopTest {

    @Test
    public void min_order_amount_check() {
        Shop shop = aShop().minOrderAmount(Money.wons(15_000)).build();

        MatcherAssert.assertThat(shop.isValidOrderAmount(Money.wons(14_000)), is(false));
        MatcherAssert.assertThat(shop.isValidOrderAmount(Money.wons(15_000)), is(true));
        MatcherAssert.assertThat(shop.isValidOrderAmount(Money.wons(16_000)), is(true));
    }

    @Test
    public void commission() {
        Shop shop = aShop()
                .commissionRate(Ratio.valueOf(0.1))
                .commission(Money.ZERO)
                .build();

        shop.billCommissionFee(Money.wons(1_000));

        MatcherAssert.assertThat(shop.getCommission(), is(Money.wons(100)));
    }
}
