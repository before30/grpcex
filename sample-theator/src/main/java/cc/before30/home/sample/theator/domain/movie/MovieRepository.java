package cc.before30.home.sample.theator.domain.movie;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MovieRepository
 *
 * @author before30
 * @since 2019-07-01
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findByTitle(String title);

}
