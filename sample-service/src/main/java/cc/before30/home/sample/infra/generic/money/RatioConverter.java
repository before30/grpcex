package cc.before30.home.sample.infra.generic.money;

import cc.before30.home.sample.domain.generic.money.Ratio;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * RatioConverter
 *
 * @author before30
 * @since 2019-06-23
 */

@Converter(autoApply = true)
public class RatioConverter implements AttributeConverter<Ratio, Double> {

    @Override
    public Double convertToDatabaseColumn(Ratio ratio) {
        return ratio.getRate();
    }

    @Override
    public Ratio convertToEntityAttribute(Double rate) {
        return Ratio.valueOf(rate);
    }
}
