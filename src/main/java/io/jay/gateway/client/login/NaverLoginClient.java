package io.jay.gateway.client.login;

import io.jay.gateway.client.login.response.NaverTokenResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface NaverLoginClient {

    @GetExchange("/token")
    Mono<NaverTokenResponse> getToken(@RequestParam Map<String, String> params);
}
