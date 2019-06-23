package cc.before30.home.sample.domain.order;

import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.generic.money.Ratio;
import cc.before30.home.sample.domain.shop.Shop;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static cc.before30.home.sample.domain.Fixtures.*;
import static org.hamcrest.core.Is.is;

/**
 * OrderTest
 *
 * @author before30
 * @since 2019-06-23
 */
class OrderTest {

    @Test
    public void order_fail_when_shop_closed() {
        Order order = anOrder().shop(aShop().open(false).build()).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.place());
    }

    @Test
    public void order_finished() {
        Order order = anOrder().status(Order.OrderStatus.ORDERED).build();
        order.payed();
        MatcherAssert.assertThat(order.getOrderStatus(), is(Order.OrderStatus.PAYED));
    }

    @Test
    public void delivery_finished() {
        Shop shop = aShop()
                .commissionRate(Ratio.valueOf(0.02))
                .commission(Money.ZERO)
                .build();

        Order order = anOrder()
                .shop(shop)
                .status(Order.OrderStatus.PAYED)
                .items(Arrays.asList(
                        anOrderLineItem()
                                .count(1)
                                .groups(Arrays.asList(
                                        anOrderOptionGroup()
                                                .options(Arrays.asList(anOrderOption().price(Money.wons(10000)).build())).build()))
                                .build()))
                .build();

        order.delivered();
        MatcherAssert.assertThat(order.getOrderStatus(), is(Order.OrderStatus.DELIVERED));
        MatcherAssert.assertThat(shop.getCommission(), is(Money.wons(200)));
    }

}