package cc.before30.home.sample.domain;

import cc.before30.home.sample.infra.generic.time.LocalDateTimePersistenceConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity
 *
 * @author before30
 * @since 2019-06-23
 */

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity implements Serializable {

    @CreatedDate
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @Column(name = "LAST_MODIFIED_AT", updatable = true)
    private LocalDateTime lastModifiedDateTime;
}
