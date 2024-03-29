package cc.before30.home.sample.domain.order;

import cc.before30.home.sample.domain.BaseEntity;
import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.shop.Menu;
import cc.before30.home.sample.domain.shop.OptionGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * OrderLineItem
 *
 * @author before30
 * @since 2019-06-23
 */

@Entity
@Table(name = "ORDER_LINE_ITEMS")
@Getter
@NoArgsConstructor
public class OrderLineItem extends BaseEntity {
    private static final long serialVersionUID = -276077434358933624L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_LINE_ITEM_ID")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "MENU_ID")
//    private Menu menu;
    @Column(name = "MENU_ID")
    private Long menuId;


    @Column(name = "FOOD_NAME")
    private String name;

    @Column(name = "FOOD_COUNT")
    private int count;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_LINE_ITEM_ID")
    private List<OrderOptionGroup> groups = new ArrayList<>();

    public OrderLineItem(Long menuId, String name, int count, List<OrderOptionGroup> groups) {
        this(null, menuId, name, count, groups);
    }

    @Builder
    public OrderLineItem(Long id, Long menuId, String name, int count, List<OrderOptionGroup> groups) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.count = count;
        this.groups.addAll(groups);
    }

    public Money calculatePrice() {
        return Money.sum(groups, OrderOptionGroup::calculatePrice).times(count);
    }

    private List<OptionGroup> convertToOptionGroups() {
        return groups.stream().map(OrderOptionGroup::convertToOptionGroup).collect(toList());
    }

//    public void validate() {
//        menu.validateOrder(name, convertToOptionGroups());
//    }

}
