package com.fuyun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUserDto {
    private Long id;
    private String login;
    private String name;
    private String email;
    private String avatar_url;
    private String bio;
    private String location;
    private String blog;
    private String company;
}