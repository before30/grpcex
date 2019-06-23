package cc.before30.home.sample.domain.order;

import cc.before30.home.sample.domain.BaseEntity;
import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.domain.shop.Shop;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "SHOP_ID")
    private Shop shop;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Column(name = "ORDERED_TIME")
    private LocalDateTime orderedTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus orderStatus;

    public Order(Long userId, Shop shop, List<OrderLineItem> items) {
        this(userId, shop, items, LocalDateTime.now(), null);
    }

    @Builder
    public Order(Long userId, Shop shop, List<OrderLineItem> items, LocalDateTime orderedTime, OrderStatus status) {
        this.userId = userId;
        this.shop = shop;
        this.orderedTime = orderedTime;
        this.orderStatus = status;
        this.orderLineItems.addAll(items);
    }

    public void place() {
        validate();
        ordered();
    }

    private void validate() {
        if (orderLineItems.isEmpty()) {
            throw new IllegalStateException("주문 항목이 비어 있습니다.");
        }

        if (!shop.isOpen()) {
            throw new IllegalArgumentException("가게가 영업중이 아닙니다.");
        }

        if (!shop.isValidOrderAmount(calculateTotalPrice())) {
            throw new IllegalStateException(String.format("최소 주문 금액 %s 이상을 주문해주세요.", shop.getMinOrderAmount()));
        }

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.validate();
        }
    }

    private void ordered() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void payed() {
        this.orderStatus = OrderStatus.PAYED;
    }

    public void delivered() {
        this.orderStatus = OrderStatus.DELIVERED;
        this.shop.billCommissionFee(calculateTotalPrice());
    }

    private Money calculateTotalPrice() {
        return Money.sum(orderLineItems, OrderLineItem::calculatePrice);
    }
}
