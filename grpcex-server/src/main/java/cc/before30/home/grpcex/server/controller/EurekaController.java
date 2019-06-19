package cc.before30.home.grpcex.server.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * EurekaController
 *
 * @author before30
 * @since 2019-06-20
 */
@RestController
public class EurekaController {

    private final EurekaClient eurekaClient;

    public EurekaController(@Qualifier("eurekaClient") EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @GetMapping("/hello")
    public String hello() {
        Map<String, String> map = new HashMap<>();
        map.put("grpc.port", "1234");
        eurekaClient.getApplicationInfoManager().registerAppMetadata(map);
        return "world";
    }


}
