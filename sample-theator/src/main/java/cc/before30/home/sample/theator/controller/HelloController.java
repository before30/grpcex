package cc.before30.home.sample.theator.controller;

import cc.before30.home.sample.theator.domain.movie.Movie;
import cc.before30.home.sample.theator.service.movie.BackofficeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HelloController
 *
 * @author before30
 * @since 2019-07-01
 */

@RestController
public class HelloController {
    private final BackofficeService backofficeService;

    public HelloController(BackofficeService backofficeService) {
        this.backofficeService = backofficeService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "world";
    }

    @GetMapping("/movies/{title}")
    public Movie movies(@PathVariable String title) {
        return backofficeService.retrieveMovie(title);
    }
}
