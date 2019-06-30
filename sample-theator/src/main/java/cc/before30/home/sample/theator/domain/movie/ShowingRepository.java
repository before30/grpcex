package cc.before30.home.sample.theator.domain.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ShowingRepository
 *
 * @author before30
 * @since 2019-07-01
 */
public interface ShowingRepository extends JpaRepository<Showing, Long> {
    List<Showing> findAllByMovieId(Long movieId);
}
