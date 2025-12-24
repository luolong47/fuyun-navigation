package com.fuyun.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String avatarUrl;
    private String displayName;
    private String bio;
    private String location;
    private String blogUrl;
    private String company;
    private String token;
}