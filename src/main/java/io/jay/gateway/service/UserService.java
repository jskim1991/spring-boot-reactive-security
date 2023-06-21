package io.jay.gateway.service;

import io.jay.gateway.controller.LoginResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<LoginResponse> login(String code, String state);

    Mono<String> authorize();
}
