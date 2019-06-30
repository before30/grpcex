package cc.before30.home.sample.theator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Discount
 *
 * @author before30
 * @since 2019-06-30
 */

@Entity
@Table(name = "DISCOUNTS")
@Getter
@NoArgsConstructor
public class Discount {

    public enum DiscountType {A, B, C}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISCOUNT_ID")
    private Long id;

    @Column(name = "MOVIE_ID")
    private Long movieId;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCOUNT_TYPE")
    private DiscountType discountType;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "PERCENT")
    private Long percent;

    @Builder
    public Discount(Long id, Long movieId, DiscountType discountType, Long amount, Long percent) {
        this.id = id;
        this.movieId = movieId;
        this.discountType = discountType;
        this.amount = amount;
        this.percent = percent;
    }
}
