package io.jay.gateway.filter;

import io.jay.gateway.domain.AuthUserDetails;
import io.jay.gateway.domain.User;
import io.jay.gateway.jwt.JsonWebTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JsonWebTokenAuthenticationFilter implements WebFilter {

    private final JsonWebTokenUtil jwtUtil;
    private final ReactiveAuthenticationManager authenticationManager;

    public JsonWebTokenAuthenticationFilter(JsonWebTokenUtil jwtUtil, ReactiveAuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            var claims = jwtUtil.parseToken(jwt);
            var user = new User(claims.getSubject());

            /* NOTE: not authenticated until authenticationManager authenticates */
            var auth = new UsernamePasswordAuthenticationToken(new AuthUserDetails(user), null);

            return authenticationManager.authenticate(auth)
                    .flatMap(authentication -> chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)));
        }
        return chain.filter(exchange);
    }
}
