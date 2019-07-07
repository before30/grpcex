package cc.before30.home.grpcex.test.microservice.order;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * OrderReceiver
 *
 * @author before30
 * @since 2019-07-07
 */

@Slf4j
@Component
public class OrderReceiver implements ChannelAwareMessageListener {

    public static final long EVENT_ITEM_ID = 16332L;
    private CountDownLatch latch = new CountDownLatch(1);
    private final OrderRepository orderRepository;
    private final Jackson2JsonMessageConverter converter;

    public OrderReceiver(OrderRepository orderRepository, Jackson2JsonMessageConverter converter) {
        this.orderRepository = orderRepository;
        this.converter = converter;
    }

    private void sleep(Long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    public CountDownLatch getLatch() {
        return this.latch;
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        CustomMessage customMessage = (CustomMessage)converter.fromMessage(message, CustomMessage.class);
        log.info("received ::: " + customMessage.getId());
        this.orderRepository.save(new EventOrder(EVENT_ITEM_ID, customMessage.getId()));

        sleep(500L);
        this.latch.countDown();
    }
}
