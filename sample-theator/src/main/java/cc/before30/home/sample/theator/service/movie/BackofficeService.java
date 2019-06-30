package cc.before30.home.sample.theator.service.movie;

import cc.before30.home.sample.theator.domain.movie.Movie;
import cc.before30.home.sample.theator.domain.movie.MovieRepository;
import cc.before30.home.sample.theator.domain.movie.Showing;
import cc.before30.home.sample.theator.domain.movie.ShowingRepository;
import cc.before30.home.sample.theator.domain.reservation.Customer;
import cc.before30.home.sample.theator.domain.reservation.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * BackofficeService
 *
 * @author before30
 * @since 2019-06-30
 */
@Service
public class BackofficeService {

    private final MovieRepository movieRepository;
    private final ShowingRepository showingRepository;
    private final CustomerRepository customerRepository;

    public BackofficeService(MovieRepository movieRepository,
                             ShowingRepository showingRepository,
                             CustomerRepository customerRepository) {
        this.movieRepository = movieRepository;
        this.showingRepository = showingRepository;
        this.customerRepository = customerRepository;
    }

    public Movie createMovie(@Nonnull String title,
                             @Nonnull Long runningTime,
                             @Nonnull Long price) {
        Movie movie = Movie.builder()
                .title(title)
                .runningTime(runningTime)
                .price(price).build();

        movieRepository.save(movie);
        return movie;
    }

    public Movie retrieveMovie(@Nonnull String title) {
        return movieRepository.findByTitle(title);
    }

    public List<Showing> getAllShowing(@Nonnull Movie movie) {
        return showingRepository.findAllByMovieId(movie.getId());
    }

    public Showing addShowing(@Nonnull Movie movie, @Nonnull int sequence, LocalDateTime showingStartTime) {
        Showing showing = Showing.builder()
                .movieId(movie.getId())
                .sequence(sequence)
                .showingStartTime(showingStartTime)
                .build();
        showingRepository.save(showing);

        return showing;
    }
}
