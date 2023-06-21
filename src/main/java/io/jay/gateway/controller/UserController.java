package io.jay.gateway.controller;

import io.jay.gateway.domain.AuthUserDetails;
import io.jay.gateway.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

@Controller
@ResponseBody
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public Mono<User> getUser(@AuthenticationPrincipal AuthUserDetails user) {
        return Mono.just(user.user());
    }
}
