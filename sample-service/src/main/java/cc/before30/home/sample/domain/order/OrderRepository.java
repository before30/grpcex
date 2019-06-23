package cc.before30.home.sample.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OrderRepository
 *
 * @author before30
 * @since 2019-06-23
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
