package com.example.travelDiary.common.auth.service;


import com.example.travelDiary.common.auth.domain.AuthUser;
import com.example.travelDiary.common.auth.dto.RegistrationRequest;
import com.example.travelDiary.common.auth.repository.AuthUserRepository;
import com.example.travelDiary.common.auth.domain.Role;
import com.example.travelDiary.common.auth.v2.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtProvider jwtProvider;
    private final String PROVIDER = "APP";
    private final Role USER = Role.USER;

    @Autowired
    public AuthUserServiceImpl(AuthUserRepository userRepository, PasswordEncoder passwordEncoder, TokenBlacklistService tokenBlacklistService, JwtProvider jwtProvider) {

        this.authUserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtProvider = jwtProvider;
    }

    public AuthUser register(RegistrationRequest registrationRequest) {
        if (authUserRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new RuntimeException("User with same Username already exists");
        }

        AuthUser user = AuthUser.builder()
                .username(registrationRequest.getUsername())
                .saltedPassword(passwordEncoder.encode(registrationRequest.getPassword()))
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getUsername())
                .role(USER)
                .createdAt(Instant.now())
                .provider(PROVIDER)
                .build();

        AuthUser registeredUser = authUserRepository.save(user);
        user.setProviderId(String.valueOf(registeredUser.getId()));
        return authUserRepository.save(user);
    }

    public void logout(String token) {
        long expirationTime = jwtProvider.getExpirationTime(token);
        tokenBlacklistService.blacklistToken(token, expirationTime);
    }

}
