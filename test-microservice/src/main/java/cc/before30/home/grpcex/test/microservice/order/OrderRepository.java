package cc.before30.home.grpcex.test.microservice.order;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OrderRepository
 *
 * @author before30
 * @since 2019-07-07
 */
public interface OrderRepository extends JpaRepository<EventOrder, Long> {
}
