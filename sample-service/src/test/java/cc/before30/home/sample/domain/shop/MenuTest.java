package cc.before30.home.sample.domain.shop;

import cc.before30.home.sample.domain.Fixtures;
import cc.before30.home.sample.domain.generic.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static cc.before30.home.sample.domain.Fixtures.*;

/**
 * MenuTest
 *
 * @author before30
 * @since 2019-06-23
 */
public class MenuTest {

    @Test
    public void menu_name_change_error() {
        Menu menu = aMenu().name("삼겹살").build();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> menu.validateOrder("오겹살", Arrays.asList(anOptionGroup().build())));
    }

    @Test
    public void option_group_name_change_error() {
        Menu menu = aMenu().basic(anOptionGroupSpec().name("기본").build()).build();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> menu.validateOrder("",
                        Arrays.asList(anOptionGroup().name("기본 메뉴").build())));
    }

    @Test
    public void option_name_change_error() {
        Menu menu = aMenu()
                .basic(anOptionGroupSpec().options(
                        Arrays.asList(anOptionSpec().name("1인분").build())
                        ).build()
                ).build();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> menu.validateOrder("",
                        Arrays.asList(anOptionGroup().options(
                                Arrays.asList(anOption().name("혼밥").build())
                        ).build())));
    }

    @Test
    public void option_price_change_error() {
        Menu menu = aMenu()
                .basic(anOptionGroupSpec()
                        .options(Arrays.asList(anOptionSpec().name("1인분").price(Money.wons(12_000)).build()))
                        .build())
                .build();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> menu.validateOrder("",
                        Arrays.asList(anOptionGroup().options(
                                Arrays.asList(anOption().name("1인분").price(Money.wons(10_000)).build())
                        ).build())));
    }
}
