package cc.before30.home.sample.theator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Rule
 *
 * @author before30
 * @since 2019-06-30
 */

@Entity
@Table(name = "RULES")
@Getter
@NoArgsConstructor
public class Rule {

    public enum RuleType { A, B, C}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RULE_ID")
    private Long id;

    @Column(name = "DISCOUNT_ID")
    private Long discountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "RULE_TYPE")
    private RuleType ruleType;

    @Column(name = "DAY_OF_WEEK")
    private LocalDateTime dayOfWeek;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Builder
    public Rule(Long id, Long discountId, RuleType ruleType, LocalDateTime dayOfWeek,
                LocalDateTime startTime, LocalDateTime endTime, Integer sequence) {
        this.id = id;
        this.discountId = discountId;
        this.ruleType = ruleType;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sequence = sequence;
    }


}
