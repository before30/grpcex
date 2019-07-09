package cc.before30.home.grpcex.client2.config;

import lombok.Getter;
import lombok.Setter;

/**
 * GrpcClientProperties
 *
 * @author before30
 * @since 2019-07-09
 */

@Setter
@Getter
public class GrpcClientProperties {

    private String name = "default";

    private String address = "127.0.0.1";

    private int port = 6565;

}
