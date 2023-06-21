package io.jay.gateway.client;

import io.jay.gateway.client.login.NaverLoginClient;
import io.jay.gateway.client.user.NaverUserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class NaverHttpProxy {

    @Bean
    public NaverLoginClient naverLoginClient(WebClient.Builder builder) {
        var wca = WebClientAdapter.forClient(builder
                .baseUrl("https://nid.naver.com/oauth2.0")
                .build());
        return HttpServiceProxyFactory.builder()
                .clientAdapter(wca)
                .build()
                .createClient(NaverLoginClient.class);
    }

    @Bean
    public NaverUserClient naverUserClient(WebClient.Builder builder) {
        var wca = WebClientAdapter.forClient(builder
                .baseUrl("https://openapi.naver.com/v1/nid")
                .build());
        return HttpServiceProxyFactory.builder()
                .clientAdapter(wca)
                .build()
                .createClient(NaverUserClient.class);
    }
}
