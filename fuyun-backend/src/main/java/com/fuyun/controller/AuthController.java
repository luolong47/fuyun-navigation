package com.fuyun.controller;

import com.fuyun.dto.GitHubUserDto;
import com.fuyun.dto.UserDto;
import com.fuyun.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String githubClientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    @GetMapping("/github/url")
    public Mono<ResponseEntity<String>> getGithubAuthUrl() {
        String authUrl = String.format(
            "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=user:email",
            githubClientId,
            redirectUri
        );
        return Mono.just(ResponseEntity.ok(authUrl));
    }

    @GetMapping("/github/callback")
    public Mono<ResponseEntity<Object>> githubCallback(
            @RequestParam("code") String code,
            ServerHttpRequest request) {
        
        log.info("GitHub回调接收到code: {}", code);
        
        return getAccessToken(code)
                .flatMap(this::getGitHubUser)
                .flatMap(userService::authenticateGitHubUser)
                .flatMap(user -> {
                    // 重定向到前端首页，并携带token
                    String redirectUrl = "http://localhost:9000/?token=" + user.getToken();
                    log.info("重定向到前端首页: {}", redirectUrl);
                    return Mono.just(ResponseEntity.status(302)
                            .location(URI.create(redirectUrl))
                            .<Object>build());
                })
                .onErrorResume(e -> {
                    log.error("GitHub登录失败", e);
                    // 失败时重定向到前端首页，不携带token
                    String redirectUrl = "http://localhost:9000/";
                    return Mono.just(ResponseEntity.status(302)
                            .location(URI.create(redirectUrl))
                            .<Object>build());
                });
    }

    private Mono<String> getAccessToken(String code) {
        String tokenUrl = "https://github.com/login/oauth/access_token";
        
        return webClient.post()
                .uri(URI.create(tokenUrl))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("client_id=" + githubClientId + 
                          "&client_secret=" + githubClientSecret +
                          "&code=" + code)
                .retrieve()
                .bodyToMono(GitHubTokenResponse.class)
                .map(GitHubTokenResponse::getAccess_token);
    }

    private Mono<GitHubUserDto> getGitHubUser(String accessToken) {
        return webClient.get()
                .uri("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .header("User-Agent", "Fuyun-Navigation")
                .retrieve()
                .bodyToMono(GitHubUserDto.class);
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<UserDto>> getCurrentUser(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        return userService.getUserByToken(token)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("获取当前用户失败", e);
                    return Mono.just(ResponseEntity.status(401).build());
                });
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<String>> logout() {
        return Mono.just(ResponseEntity.ok("退出成功"));
    }

    private static class GitHubTokenResponse {
        private String access_token;
        private String token_type;
        private String scope;

        public String getAccess_token() { return access_token; }
        public void setAccess_token(String access_token) { this.access_token = access_token; }
        public String getToken_type() { return token_type; }
        public void setToken_type(String token_type) { this.token_type = token_type; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
    }
}