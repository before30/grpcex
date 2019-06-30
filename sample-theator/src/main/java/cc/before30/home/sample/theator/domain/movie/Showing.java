package cc.before30.home.sample.theator.domain.movie;

import cc.before30.home.sample.theator.util.LocalDateTimePersistenceConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Showing
 *
 * @author before30
 * @since 2019-06-30
 */

@Entity
@Table(name = "SHOWINGS")
@Getter
@NoArgsConstructor
public class Showing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOWING_ID")
    private Long id;

    @Column(name = "MOVIE_ID")
    private Long movieId;

    @Column(name = "SEQUENCE")
    private int sequence;

    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @Column(name = "SHOWING_START_TIME")
    private LocalDateTime showingStartTime;

    @Builder
    public Showing(Long id, Long movieId, int sequence, LocalDateTime showingStartTime) {
        this.id = id;
        this.movieId = movieId;
        this.sequence = sequence;
        this.showingStartTime = showingStartTime;
    }
}
