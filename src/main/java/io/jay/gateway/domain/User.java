package io.jay.gateway.domain;

public record User(String id,
        String naverId,
        String name,
        String nickname,
        String email) {
    public User(String id) {
        this(id, null, null, null, null);
    }
}
