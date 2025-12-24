package com.fuyun.service;

import com.fuyun.dto.GitHubUserDto;
import com.fuyun.dto.UserDto;
import com.fuyun.entity.User;
import com.fuyun.repository.UserRepository;
import com.fuyun.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Mono<UserDto> authenticateGitHubUser(GitHubUserDto githubUser) {
        return Mono.fromCallable(() -> {
            Optional<User> existingUserOpt = userRepository.findByGithubId(githubUser.getId().toString());
            
            User user;
            if (existingUserOpt.isPresent()) {
                user = existingUserOpt.get();
                updateUserFromGitHub(user, githubUser);
                log.info("更新GitHub用户: {}", user.getUsername());
            } else {
                user = createUserFromGitHub(githubUser);
                log.info("创建新GitHub用户: {}", user.getUsername());
            }
            
            user = userRepository.save(user);
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail());
            
            return convertToUserDto(user, token);
        });
    }

    private User createUserFromGitHub(GitHubUserDto githubUser) {
        User user = new User();
        user.setGithubId(githubUser.getId().toString());
        user.setUsername(githubUser.getLogin());
        user.setEmail(githubUser.getEmail() != null ? githubUser.getEmail() : githubUser.getLogin() + "@github.local");
        user.setAvatarUrl(githubUser.getAvatar_url());
        user.setDisplayName(githubUser.getName() != null ? githubUser.getName() : githubUser.getLogin());
        user.setBio(githubUser.getBio());
        user.setLocation(githubUser.getLocation());
        user.setBlogUrl(githubUser.getBlog());
        user.setCompany(githubUser.getCompany());
        user.setIsActive(true);
        return user;
    }

    private void updateUserFromGitHub(User user, GitHubUserDto githubUser) {
        user.setAvatarUrl(githubUser.getAvatar_url());
        user.setDisplayName(githubUser.getName() != null ? githubUser.getName() : githubUser.getLogin());
        user.setBio(githubUser.getBio());
        user.setLocation(githubUser.getLocation());
        user.setBlogUrl(githubUser.getBlog());
        user.setCompany(githubUser.getCompany());
        user.setUpdatedAt(LocalDateTime.now());
    }

    private UserDto convertToUserDto(User user, String token) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setAvatarUrl(user.getAvatarUrl());
        userDto.setDisplayName(user.getDisplayName());
        userDto.setBio(user.getBio());
        userDto.setLocation(user.getLocation());
        userDto.setBlogUrl(user.getBlogUrl());
        userDto.setCompany(user.getCompany());
        userDto.setToken(token);
        return userDto;
    }

    public Mono<UserDto> getUserByToken(String token) {
        return Mono.fromCallable(() -> {
            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("无效的token");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                throw new RuntimeException("用户不存在");
            }
            
            User user = userOpt.get();
            return convertToUserDto(user, token);
        });
    }
}