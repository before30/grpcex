package cc.before30.home.grpcex.client.resolver;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.Application;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EurekaNameResolver
 *
 * @author before30
 * @since 2019-06-21
 */
public class EurekaNameResolver extends NameResolver {
    private final EurekaClient eurekaClient;

    public EurekaNameResolver(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public String getServiceAuthority() {
        return "grpc_server";
    }

    @Override
    public void start(Listener listener) {
        update(listener);
    }

    private void update(Listener listener) {
        Application application = eurekaClient.getApplication("grpc_server");

        List<EquivalentAddressGroup> servers = application.getInstances().stream().map(instanceInfo -> {
            String s = instanceInfo.getMetadata().get("grpc.port");
            int port = Integer.parseInt(s);

            return new EquivalentAddressGroup(new InetSocketAddress("127.0.0.1", port), Attributes.EMPTY);
        }).collect(Collectors.toList());

        listener.onAddresses(servers, Attributes.EMPTY);
    }

    @Override
    public void shutdown() {

    }
}
