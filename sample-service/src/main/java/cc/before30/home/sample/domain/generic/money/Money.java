package cc.before30.home.sample.domain.generic.money;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * Money
 *
 * @author before30
 * @since 2019-06-23
 */

@Getter
public class Money {
    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static <T> Money sum(Collection<T> bags, Function<T, Money> monetary) {
        return bags.stream()
                .map(b -> monetary.apply(b))
                .reduce(Money.ZERO, Money::plus);
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public Money divide(double divisor) {
        return new Money(amount.divide(BigDecimal.valueOf(divisor)));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

    public Long longValue() {
        return amount.longValue();
    }

    public Double doubleValue() {
        return amount.doubleValue();
    }

    @Override
    public String toString() {
        return amount.toString() + "원";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Money)) {
            return false;
        }

        Money that = (Money)obj;
        return Objects.equals(amount.doubleValue(), that.amount.doubleValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }
}
