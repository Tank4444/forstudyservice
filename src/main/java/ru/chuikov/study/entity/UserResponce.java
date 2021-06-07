package ru.chuikov.study.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserResponce {

    static public UserResponce fromUser(User user){
        return UserResponce.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .userStatus(user.getUserStatus())
                .created(user.getCreated())
                .build();
    }

    private long id;

    private String username;

    private String email;

    private Role role;

    private UserStatus userStatus;

    private Date created;
}
