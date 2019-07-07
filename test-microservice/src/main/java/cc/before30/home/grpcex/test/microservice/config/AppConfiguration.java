package cc.before30.home.grpcex.test.microservice.config;

import cc.before30.home.grpcex.test.microservice.order.OrderReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

/**
 * AppConfiguration
 *
 * @author before30
 * @since 2019-07-07
 */

@Configuration
public class AppConfiguration {

    public static final String EVENT_QUEUE = "event-queue";
    public static final String EVENT_TOPIC = "event-topic";
    public static final String EVENT_APPLY_KEY = "event-apply-success-size";
    public static final String EVENT_APPLY_LIST = "event-apply-list";

    @Bean
    public ReactiveRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                                       ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer defaultSerializer = new StringRedisSerializer();
        RedisSerializationContext serializationContext = RedisSerializationContext
                .newSerializationContext(defaultSerializer)
                .hashValue(jackson2JsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveValueOperations reactiveValueOperations(ReactiveRedisTemplate reactiveRedisTemplate) {
        return reactiveRedisTemplate.opsForValue();
    }

    @Bean
    public ReactiveListOperations reactiveListOperations(ReactiveRedisTemplate reactiveRedisTemplate) {
        return reactiveRedisTemplate.opsForList();
    }

    @Bean
    public Queue queue() {
        return new Queue(EVENT_QUEUE, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EVENT_TOPIC);
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    OrderReceiver orderReceiver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(EVENT_QUEUE);
        container.setMessageListener(orderReceiver);
        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }
}
