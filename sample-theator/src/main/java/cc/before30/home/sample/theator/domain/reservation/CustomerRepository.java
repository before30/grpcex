package cc.before30.home.sample.theator.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CustomerRepository
 *
 * @author before30
 * @since 2019-07-01
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
