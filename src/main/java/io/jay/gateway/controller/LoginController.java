package io.jay.gateway.controller;

import io.jay.gateway.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

@Controller
@ResponseBody
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/token")
    public Mono<LoginResponse> login(@RequestParam String code, @RequestParam String state) {
        return userService.login(code, state);
    }

    @GetMapping("/authorize")
    public Mono<String> authorize() {
        return userService.authorize();
    }
}
