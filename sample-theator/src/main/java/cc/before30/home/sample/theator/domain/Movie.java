package cc.before30.home.sample.theator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Movie
 *
 * @author before30
 * @since 2019-06-30
 */

@Entity
@Table(name = "MOVIES")
@Getter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "RUNNING_TIME")
    private Long runningTime;

    @Column(name = "FEE")
    private Long fee;

    @Builder
    public Movie(Long id, String title, Long runningTime, Long fee) {
        this.id = id;
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
    }
}
