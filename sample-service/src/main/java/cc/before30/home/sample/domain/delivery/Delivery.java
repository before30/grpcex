package cc.before30.home.sample.domain.delivery;

import cc.before30.home.sample.domain.BaseEntity;
import cc.before30.home.sample.domain.order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Delivery
 *
 * @author before30
 * @since 2019-06-23
 */

@Entity
@Table(name = "DELIVERIES")
@Getter
@NoArgsConstructor
public class Delivery extends BaseEntity {
    private static final long serialVersionUID = 3818821305788251871L;

    enum DeliveryStatus { DELIVERING, DELIVERED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private DeliveryStatus deliveryStatus;

    public static Delivery started(Order order) {
        return new Delivery(order, DeliveryStatus.DELIVERING);
    }

    public Delivery(Order order, DeliveryStatus deliveryStatus) {
        this.order = order;
        this.deliveryStatus = deliveryStatus;
    }

    public void complete() {
        this.deliveryStatus = DeliveryStatus.DELIVERED;
    }

}
