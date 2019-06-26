package cc.before30.home.sample.service.order;

import cc.before30.home.sample.domain.delivery.Delivery;
import cc.before30.home.sample.domain.delivery.DeliveryRepository;
import cc.before30.home.sample.domain.order.Order;
import cc.before30.home.sample.domain.order.OrderRepository;
import cc.before30.home.sample.domain.order.OrderValidator;
import cc.before30.home.sample.domain.shop.Shop;
import cc.before30.home.sample.domain.shop.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderService
 *
 * @author before30
 * @since 2019-06-23
 */

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ShopRepository shopRepository;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;

    public OrderService(OrderMapper orderMapper,
                        OrderRepository orderRepository,
                        DeliveryRepository deliveryRepository,
                        ShopRepository shopRepository,
                        OrderValidator orderValidator) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderValidator = orderValidator;
        this.shopRepository = shopRepository;
    }

    @Transactional
    public void placeOrder(Cart cart) {
        Order order = orderMapper.mapFrom(cart);
        order.place(orderValidator);
        orderRepository.save(order);
    }

    @Transactional
    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.payed();

        Delivery delivery = Delivery.started(order.getId());
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        Shop shop = shopRepository.findById(order.getShopId()).orElseThrow(IllegalArgumentException::new);
        Delivery delivery = deliveryRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);

        order.delivered();
        shop.billCommissionFee(order.calculateTotalPrice());
        delivery.complete();
    }
}
