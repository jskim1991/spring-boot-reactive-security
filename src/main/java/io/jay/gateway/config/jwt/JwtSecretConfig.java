package io.jay.gateway.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecretConfig {

    private final String secretKey;

    public JwtSecretConfig(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public JwtSecret jwtSecret() {
        return new JwtSecret(secretKey);
    }
}
