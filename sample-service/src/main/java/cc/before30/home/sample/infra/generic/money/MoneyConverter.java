package cc.before30.home.sample.infra.generic.money;

import cc.before30.home.sample.domain.generic.money.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * MoneyConverter
 *
 * @author before30
 * @since 2019-06-23
 */

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Long> {

    @Override
    public Long convertToDatabaseColumn(Money money) {
        return money.longValue();
    }

    @Override
    public Money convertToEntityAttribute(Long amount) {
        return Money.wons(amount);
    }
}
