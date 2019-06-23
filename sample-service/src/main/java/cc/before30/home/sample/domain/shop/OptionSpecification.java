package cc.before30.home.sample.domain.shop;

import cc.before30.home.sample.domain.BaseEntity;
import cc.before30.home.sample.domain.generic.money.Money;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * OptionSpecification
 *
 * @author before30
 * @since 2019-06-23
 */

@Entity
@Table(name = "OPTION_SPECS")
@Getter
@NoArgsConstructor
public class OptionSpecification extends BaseEntity {
    private static final long serialVersionUID = 4050450837549692000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OPTION_SPEC_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private Money price;

    public OptionSpecification(String name, Money price) {
        this(null, name, price);
    }

    @Builder
    public OptionSpecification(Long id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    public boolean isSatisfiedBy(Option option) {
        return Objects.equals(name, option.getName()) &&
                Objects.equals(price, option.getPrice());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof OptionSpecification)) {
            return false;
        }

        OptionSpecification that = (OptionSpecification)object;
        return Objects.equals(name, that.getName()) && Objects.equals(price, that.getPrice());
    }
}
