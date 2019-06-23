package cc.before30.home.sample.domain.generic.money;

import lombok.Getter;

/**
 * Ratio
 *
 * @author before30
 * @since 2019-06-23
 */

@Getter
public class Ratio {
    private double rate;

    public static Ratio valueOf(double rate) {
        return new Ratio(rate);
    }

    public Ratio(double rate) {
        this.rate = rate;
    }

    Ratio() {}

    public Money of(Money price) {
        return price.times(rate);
    }
}
