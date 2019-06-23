package cc.before30.home.sample.domain.order;

import cc.before30.home.sample.domain.BaseEntity;
import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.shop.OptionGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * OrderOptionGroup
 *
 * @author before30
 * @since 2019-06-23
 */

@Entity
@Table(name = "ORDER_OPTION_GROUPS")
@Getter
@NoArgsConstructor
public class OrderOptionGroup extends BaseEntity {
    private static final long serialVersionUID = -2432758937478030093L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_OPTION_GROUP_ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @ElementCollection
    @CollectionTable(name="ORDER_OPTIONS", joinColumns = @JoinColumn(name="ORDER_OPTION_GROUP_ID"))
    private List<OrderOption> orderOptions;

    public OrderOptionGroup(String name, List<OrderOption> options) {
        this(null, name, options);
    }

    @Builder
    public OrderOptionGroup(Long id, String name, List<OrderOption> options) {
        this.id = id;
        this.name = name;
        this.orderOptions = options;
    }

    public Money calculatePrice() {
        return Money.sum(orderOptions, OrderOption::getPrice);
    }

    public OptionGroup convertToOptionGroup() {
        return new OptionGroup(name, orderOptions.stream().map(OrderOption::convertToOption).collect(toList()));
    }
}
