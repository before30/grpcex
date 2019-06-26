package cc.before30.home.sample.domain.order;

import cc.before30.home.sample.domain.BaseEntity;
import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.shop.Shop;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order
 *
 * @author before30
 * @since 2019-06-23
 */

@Entity
@Table(name = "ORDERS")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {
    private static final long serialVersionUID = 3008060894288277116L;

    public enum OrderStatus { ORDERED, PAYED, DELIVERED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

//    @ManyToOne
//    @JoinColumn(name = "SHOP_ID")
    @Column(name = "SHOP_ID")
    private Long shopId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Column(name = "ORDERED_TIME")
    private LocalDateTime orderedTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus orderStatus;

    public Order(Long userId, Long shopId, List<OrderLineItem> items) {
        this(userId, shopId, items, LocalDateTime.now(), null);
    }

    @Builder
    public Order(Long userId, Long shopId, List<OrderLineItem> items, LocalDateTime orderedTime, OrderStatus status) {
        this.userId = userId;
        this.shopId = shopId;
        this.orderedTime = orderedTime;
        this.orderStatus = status;
        this.orderLineItems.addAll(items);
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream().map(OrderLineItem::getId).collect(Collectors.toList());
    }

    public void place(OrderValidator orderValidator) {
        orderValidator.validate(this);
        ordered();
    }

    private void ordered() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void payed() {
        this.orderStatus = OrderStatus.PAYED;
    }

    public void delivered() {
        this.orderStatus = OrderStatus.DELIVERED;

    }

    public  Money calculateTotalPrice() {
        return Money.sum(orderLineItems, OrderLineItem::calculatePrice);
    }
}
