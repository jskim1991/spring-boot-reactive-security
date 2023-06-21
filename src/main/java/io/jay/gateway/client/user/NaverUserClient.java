package io.jay.gateway.client.user;

import io.jay.gateway.client.user.response.NaverUserResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface NaverUserClient {

    @GetExchange("/me")
    Mono<NaverUserResponse> getUserInfo(@RequestHeader Map<String, String> headers);
}
