package cc.before30.home.grpcex.test.microservice.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * CustomMessage
 *
 * @author before30
 * @since 2019-07-07
 */

@Getter
@ToString
@NoArgsConstructor
public class CustomMessage implements Serializable {
    private static final long serialVersionUID = 2759307080667652816L;

    private Long id;
    private String text;
    private Integer priority;
    private Boolean secret;

    @Builder
    public CustomMessage(@JsonProperty("id") Long id,
                         @JsonProperty("text") String text,
                         @JsonProperty("priority") Integer priority,
                         @JsonProperty("secret") Boolean secret) {
        this.id = id;
        this.text = text;
        this.priority = priority;
        this.secret = secret;
    }


}
