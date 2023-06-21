package io.jay.gateway.service;

import io.jay.gateway.client.login.NaverLoginClient;
import io.jay.gateway.client.user.NaverUserClient;
import io.jay.gateway.client.user.response.NaverUserResponse;
import io.jay.gateway.config.oauth2.NaverEndpointConfig;
import io.jay.gateway.controller.LoginResponse;
import io.jay.gateway.domain.AuthUserDetails;
import io.jay.gateway.domain.User;
import io.jay.gateway.jwt.JsonWebTokenUtil;
import io.jay.gateway.mapper.UserDataMapper;
import io.jay.gateway.repository.UserEntity;
import io.jay.gateway.repository.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class DefaultUserService implements UserService, ReactiveUserDetailsService {

    private final UserJpaRepository userJpaRepository;
    private final NaverLoginClient loginClient;
    private final NaverUserClient userClient;
    private final UserDataMapper userDataMapper;
    private final JsonWebTokenUtil jsonWebTokenUtil;
    private final NaverEndpointConfig naverEndpointConfig;
    private final StringKeyGenerator stateGenerator;

    Logger log = LoggerFactory.getLogger(DefaultUserService.class);

    public DefaultUserService(UserJpaRepository userJpaRepository, NaverLoginClient loginClient, NaverUserClient userClient, UserDataMapper userDataMapper, JsonWebTokenUtil jsonWebTokenUtil, NaverEndpointConfig naverEndpointConfig) {
        this.userJpaRepository = userJpaRepository;
        this.loginClient = loginClient;
        this.userClient = userClient;
        this.userDataMapper = userDataMapper;
        this.jsonWebTokenUtil = jsonWebTokenUtil;
        this.naverEndpointConfig = naverEndpointConfig;
        stateGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder());
    }

    @Override
    public Mono<LoginResponse> login(String code, String state) {
        log.info("code: {}, state: {}", code, state);
        var params = new HashMap<String, String>();
        params.put("grant_type", naverEndpointConfig.grantType());
        params.put("client_id", naverEndpointConfig.clientId());
        params.put("client_secret", naverEndpointConfig.clientSecret());
        params.put("code", code);
        params.put("state", state);
        return loginClient.getToken(params)
                .doOnNext(token -> log.info("token: {}", token))
                .flatMap(token -> {
                    if (token.error() != null) {
                        return Mono.error(new RuntimeException(token.error() + " " + token.errorDescription()));
                    }
                    var headers = Map.of(AUTHORIZATION, "Bearer " + token.accessToken());
                    return userClient.getUserInfo(headers)
                            .doOnNext(user -> log.info("user: {}", user))
                            .flatMap(this::saveUser);
                })
                .flatMap(user -> {
                    var userDetails = new AuthUserDetails(user);
                    return Mono.just(new LoginResponse(jsonWebTokenUtil.createAccessToken(userDetails.getUsername(), userDetails.getAuthorities())));
                });

    }

    private Mono<User> saveUser(NaverUserResponse user) {
        var userOptional = userJpaRepository.findByNaverId(user.response().id());
        if (userOptional.isEmpty()) {
            UserEntity userEntity = new UserEntity(user.response().id(), user.response().name(), user.response().nickname(), user.response().email());
            var savedUser = userJpaRepository.save(userEntity);
            return Mono.just(userDataMapper.toDomain(savedUser));
        }

        return Mono.just(userDataMapper.toDomain(userOptional.get()));
    }

    @Override
    public Mono<String> authorize() {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", naverEndpointConfig.responseType())
                .queryParam("client_id", naverEndpointConfig.clientId())
                .queryParam("client_secret", naverEndpointConfig.clientSecret())
                .queryParam("redirect_uri", URLEncoder.encode(naverEndpointConfig.redirectUri(), UTF_8))
                .queryParam("state", URLEncoder.encode(stateGenerator.generateKey(), UTF_8))
                .build();

        return Mono.just(uriComponents.toString());
    }

    @Override
    public Mono<UserDetails> findByUsername(String id) {
        return Mono.fromCallable(() -> {
                    var userOptional = userJpaRepository.findById(id);
                    return userOptional.orElseThrow(() -> new RuntimeException("User not found with id: " + id));
                }).subscribeOn(Schedulers.boundedElastic())
                .map(userEntity -> {
                    var user = userDataMapper.toDomain(userEntity);
                    return new AuthUserDetails(user);
                });
    }
}
