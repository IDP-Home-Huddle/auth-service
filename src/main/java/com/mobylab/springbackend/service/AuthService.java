package com.mobylab.springbackend.service;

import com.mobylab.springbackend.config.security.JwtGenerator;
import com.mobylab.springbackend.exception.AuthException;
import com.mobylab.springbackend.service.dto.LoginRequestDto;
import com.mobylab.springbackend.service.dto.RegisterRequestDto;
import com.mobylab.springbackend.service.dto.RegisterWithFamilyIdRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private RequestService requestService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public UUID registerWithoutFamilyId(RegisterRequestDto registerRequestDto) {
        String password = registerRequestDto.getPassword();
        registerRequestDto.setPassword(passwordEncoder.encode(password));

        ResponseEntity<UUID> response = requestService.sendPostRequest(
                "/authproxy/register",
                Collections.emptyMap(),
                registerRequestDto,
                new ParameterizedTypeReference<>(){});

        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Register request failed.");
            throw new AuthException("Register request failed.");
        }

        return response.getBody();
    }

    public void registerWithFamilyId(RegisterWithFamilyIdRequestDto registerRequestDto) {
        String password = registerRequestDto.getPassword();
        registerRequestDto.setPassword(passwordEncoder.encode(password));

        ResponseEntity<Void> response = requestService.sendPostRequest(
                "/authproxy/register/family-id",
                Collections.emptyMap(),
                registerRequestDto,
                new ParameterizedTypeReference<>(){});

        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Register with family id request failed.");
            throw new AuthException("Register with family id request failed.");
        }
    }

    public String login(LoginRequestDto loginRequestDto) {
        String password = loginRequestDto.getPassword();
        loginRequestDto.setPassword(passwordEncoder.encode(password));

        ResponseEntity<Void> response = requestService.sendPostRequest(
                "/authproxy/login",
                Collections.emptyMap(),
                loginRequestDto,
                new ParameterizedTypeReference<>(){});

        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Login request failed.");
            throw new AuthException("Login request failed.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtGenerator.generateToken(authentication);
    }
}
