package io.jay.gateway.client.user.response;

public record NaverUserResponse(
        String resultCode,
        String message,
        UserResponse response
) {
}
