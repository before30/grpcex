package cc.before30.home.sample.domain.generic.money;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Ratio
 *
 * @author before30
 * @since 2019-06-23
 */

@Getter
@NoArgsConstructor
public class Ratio {
    private double rate;

    public static Ratio valueOf(double rate) {
        return new Ratio(rate);
    }

    public Ratio(double rate) {
        this.rate = rate;
    }

    public Money of(Money price) {
        return price.times(rate);
    }
}
