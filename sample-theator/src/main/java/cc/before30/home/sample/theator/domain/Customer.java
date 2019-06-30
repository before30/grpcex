package cc.before30.home.sample.theator.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Customer
 *
 * @author before30
 * @since 2019-06-30
 */

@Entity
@Table(name = "CUSTOMERS")
@Getter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Builder
    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
