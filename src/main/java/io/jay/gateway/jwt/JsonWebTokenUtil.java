package io.jay.gateway.jwt;

import io.jay.gateway.config.jwt.JwtSecret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class JsonWebTokenUtil {

    private final SecretKey secretKey;

    public JsonWebTokenUtil(JwtSecret jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.secretInString().getBytes(UTF_8));
    }

    public String createAccessToken(String id, Collection<? extends GrantedAuthority> roles) {
        return createToken(id, roles, Duration.ofMinutes(10).toMillis());
    }

    public Claims parseToken(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
        return parser.parseClaimsJws(token)
                .getBody();
    }

    private String createToken(String id, Collection<? extends GrantedAuthority> roles, long validTime) {
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(secretKey)
                .compact();
    }
}
