package cc.before30.home.sample;

import cc.before30.home.sample.domain.generic.money.Money;
import cc.before30.home.sample.service.order.Cart;
import cc.before30.home.sample.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * SampleApplication
 *
 * @author before30
 * @since 2019-06-23
 */

@SpringBootApplication
@Slf4j
@EnableJpaAuditing
@EntityScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"cc.before30.home.sample.domain", "cc.before30.home.sample.infra"}
)
public class SampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION.");
        SpringApplication.run(SampleApplication.class, args);
        log.info("APPLICATION FINISHED.");
    }

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        Cart cart = new Cart(1L, 1L,
                new Cart.CartLineItem(1L, "삼겹살 1인세트", 2,
                        new Cart.CartOptionGroup("기본",
                                new Cart.CartOption("소(250g)", Money.wons(12_000)))));
        orderService.placeOrder(cart);
        orderService.payOrder(1L);
        orderService.deliverOrder(1L);
    }
}
