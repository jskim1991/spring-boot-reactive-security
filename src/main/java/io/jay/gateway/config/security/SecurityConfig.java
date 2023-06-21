package io.jay.gateway.config.security;

import io.jay.gateway.filter.JsonWebTokenAuthenticationFilter;
import io.jay.gateway.jwt.JsonWebTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JsonWebTokenUtil jsonWebTokenUtil, ReactiveAuthenticationManager authenticationManager) {
        return http
                .exceptionHandling(spec -> spec
                        .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(() -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        }))
                        .accessDeniedHandler((exchange, denied) -> Mono.fromRunnable(() -> {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        })))
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // STATELESS
                .authorizeExchange(spec -> spec
                        .pathMatchers("/login/**").permitAll()
                        .anyExchange().authenticated()
                )
                /* NOTE: If filter is a Spring bean then there is no need to manually add like this.
                         Otherwise, it will execute the filter twice per request */
                .addFilterAt(new JsonWebTokenAuthenticationFilter(jsonWebTokenUtil, authenticationManager), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }
}
