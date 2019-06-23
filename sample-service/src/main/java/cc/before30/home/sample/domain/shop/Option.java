package cc.before30.home.sample.domain.shop;

import cc.before30.home.sample.domain.generic.money.Money;
import lombok.Builder;
import lombok.Data;

/**
 * Option
 *
 * @author before30
 * @since 2019-06-23
 */

@Data
public class Option {
    private String name;
    private Money price;

    @Builder
    public Option(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
