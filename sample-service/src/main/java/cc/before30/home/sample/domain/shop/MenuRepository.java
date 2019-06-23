package cc.before30.home.sample.domain.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MenuRepository
 *
 * @author before30
 * @since 2019-06-23
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
