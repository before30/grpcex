package cc.before30.home.grpcex.test.microservice.event;

import cc.before30.home.grpcex.test.microservice.config.AppConfiguration;
import cc.before30.home.grpcex.test.microservice.order.CustomMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;

/**
 * EventService
 *
 * @author before30
 * @since 2019-07-07
 */

@Service
public class EventService {
    private static final int PURCHASE_LIMIT = 100;

    private final ReactiveValueOperations<String, String> reactiveValueOperations;
    private final ReactiveListOperations<String, String> reactiveListOperations;
    private final RabbitTemplate rabbitTemplate;

    public EventService(ReactiveValueOperations<String, String> reactiveValueOperations,
                        ReactiveListOperations<String, String> reactiveListOperations,
                        RabbitTemplate rabbitTemplate) {
        this.reactiveValueOperations = reactiveValueOperations;
        this.reactiveListOperations = reactiveListOperations;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Boolean> apply(Long userId) {
        CustomMessage message = new CustomMessage()
                .builder()
                .id(userId)
                .text(RandomStringUtils.randomAlphanumeric(10))
                .build();

        return this.reactiveValueOperations.get(AppConfiguration.EVENT_APPLY_KEY)
                .doOnNext(size -> this.rabbitTemplate.convertAndSend(
                        AppConfiguration.EVENT_TOPIC,
                        "foo.bar.baz",
                         message
                )).filter(this::isPurchase)
                .flatMap(s -> this.reactiveValueOperations.increment(AppConfiguration.EVENT_APPLY_KEY))
                .flatMap(s -> this.reactiveListOperations.leftPush(AppConfiguration.EVENT_APPLY_LIST, userId.toString()))
                .map(add -> true)
                .defaultIfEmpty(false);
    }

    private boolean isPurchase(String size) {
        return isNull(size) || Integer.parseInt(size) < PURCHASE_LIMIT;
    }
}
