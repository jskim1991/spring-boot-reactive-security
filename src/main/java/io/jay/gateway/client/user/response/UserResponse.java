package io.jay.gateway.client.user.response;

public record UserResponse(
        String id,
        String nickname,
        String name,
        String email,
        String gender,
        String age,
        String birthday,
        String birthyear,
        String mobile
) {
}
