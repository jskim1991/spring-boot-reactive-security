package io.jay.gateway.config.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "naver")
public record NaverEndpointConfig(
        String grantType,
        String responseType,
        String clientId,
        String clientSecret,
        String state,
        String redirectUri) {
}
