package io.jay.gateway.client.login.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverTokenResponse(@JsonProperty("access_token") String accessToken,
                                 @JsonProperty("refresh_token") String refreshToken,
                                 @JsonProperty("token_type") String tokenType,
                                 @JsonProperty("expires_in") int expiresIn,
                                 @JsonProperty("error") String error,
                                 @JsonProperty("error_description") String errorDescription
) {
}
