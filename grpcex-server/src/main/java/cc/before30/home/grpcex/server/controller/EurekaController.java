package cc.before30.home.grpcex.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * EurekaController
 *
 * @author before30
 * @since 2019-06-20
 */
@RestController
public class EurekaController {
    @GetMapping("/hello")
    public String hello() {
        return "world";
    }
}
