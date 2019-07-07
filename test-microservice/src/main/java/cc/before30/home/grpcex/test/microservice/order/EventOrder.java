package cc.before30.home.grpcex.test.microservice.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

/**
 * EventOrder
 *
 * @author before30
 * @since 2019-07-07
 */

@Entity
@Data
@NoArgsConstructor
public class EventOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    private Long userId;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public EventOrder(Long itemId, Long userId) {
        this.itemId = itemId;
        this.userId = userId;

        final LocalDateTime now = LocalDateTime.now();
        this.createDate = now;
        this.updateDate = now;
    }
}
