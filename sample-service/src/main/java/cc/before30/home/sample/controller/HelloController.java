package cc.before30.home.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * @author before30
 * @since 2019-06-23
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "world";
    }
}
