package cc.before30.home.sample.theator.domain.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Reservation
 *
 * @author before30
 * @since 2019-06-30
 */

@Entity
@Table(name = "RESERVATIONS")
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "SHOWING_ID")
    private Long showingId;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "AUDIENCE_COUNT")
    private Integer audienceCount;

    @Builder
    public Reservation(Long id, Long customerId, Long showingId, Long amount, Integer audienceCount) {
        this.id = id;
        this.customerId = customerId;
        this.showingId = showingId;
        this.amount = amount;
        this.audienceCount = audienceCount;
    }
}
