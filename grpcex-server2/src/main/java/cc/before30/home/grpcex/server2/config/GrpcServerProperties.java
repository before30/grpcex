package cc.before30.home.grpcex.server2.config;

import lombok.Getter;
import lombok.Setter;

/**
 * GrpcServerProperties
 *
 * @author before30
 * @since 2019-07-08
 */

@Getter
@Setter
public class GrpcServerProperties {
    public static final int DEFAULT_GRPC_PORT = 6565;

    private Integer port = null;

    private String inProcessServerName;

    private boolean enableReflection = false;

}
