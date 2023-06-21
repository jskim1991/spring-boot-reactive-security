package io.jay.gateway.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String id;
    private String naverId;
    private String name;
    private String nickname;
    private String email;

    public UserEntity(String naverId, String name, String nickname, String email) {
        this.id = UUID.randomUUID().toString();
        this.naverId = naverId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }

    public UserEntity() {
        // for JPA
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaverId() {
        return naverId;
    }

    public void setNaverId(String naverId) {
        this.naverId = naverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
