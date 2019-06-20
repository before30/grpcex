package cc.before30.home.grpcex.client.resolver;

import com.netflix.discovery.EurekaClient;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import javax.annotation.Nullable;
import java.net.URI;

/**
 * EurekaNameResolverProvider
 *
 * @author before30
 * @since 2019-06-21
 */
public class EurekaNameResolverProvider extends NameResolverProvider {

    private final EurekaClient eurekaClient;

    public EurekaNameResolverProvider(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        return new EurekaNameResolver(eurekaClient);
    }

    @Override
    public String getDefaultScheme() {
        return "EUREKA";
    }
}
