package cc.before30.home.sample.service.order;

import cc.before30.home.sample.domain.delivery.Delivery;
import cc.before30.home.sample.domain.delivery.DeliveryRepository;
import cc.before30.home.sample.domain.order.Order;
import cc.before30.home.sample.domain.order.OrderRepository;
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
    private OrderRepository orderRepository;
    private DeliveryRepository deliveryRepository;
    private OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper,
                        OrderRepository orderRepository,
                        DeliveryRepository deliveryRepository) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public void placeOrder(Cart cart) {
        Order order = orderMapper.mapFrom(cart);
        order.place();
        orderRepository.save(order);
    }

    @Transactional
    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.payed();

        Delivery delivery = Delivery.started(order);
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.delivered();

        Delivery delivery = deliveryRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        delivery.complete();
    }
}
