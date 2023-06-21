package io.jay.gateway.mapper;

import io.jay.gateway.domain.User;
import io.jay.gateway.repository.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

    public User toDomain(UserEntity e) {
        return new User(e.getId(), e.getNaverId(), e.getName(), e.getNickname(), e.getEmail());
    }
}
