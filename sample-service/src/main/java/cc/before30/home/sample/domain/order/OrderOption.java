package cc.before30.home.sample.domain.order;

import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.shop.Option;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * OrderOption
 *
 * @author before30
 * @since 2019-06-23
 */

@Embeddable
@Getter
@NoArgsConstructor
public class OrderOption {

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private Money price;

    @Builder
    public OrderOption(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public Option convertToOption() {
        return new Option(name, price);
    }
}
