package com.mobylab.springbackend.service;

import com.mobylab.springbackend.config.security.JwtGenerator;
import com.mobylab.springbackend.service.dto.LoginDto;
import com.mobylab.springbackend.service.dto.RegisterDto;
import com.mobylab.springbackend.service.dto.RegisterResponseDto;
import com.mobylab.springbackend.service.dto.RegisterWithFamilyIdDto;
import com.mobylab.springbackend.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    public UUID register(RegisterDto registerDto) {
        String email = registerDto.getEmail();

        if (AuthUtils.isNewUser(email)) {
            RegisterWithFamilyIdDto registerWithFamilyIdDto = new RegisterWithFamilyIdDto(registerDto);

            String encodedPassword = passwordEncoder.encode(registerWithFamilyIdDto.getPassword());
            registerWithFamilyIdDto.setPassword(encodedPassword);

            return AuthUtils.registerUser(registerWithFamilyIdDto);
        }

        return null;
    }

    public void registerWithFamilyId(RegisterWithFamilyIdDto registerWithFamilyIdDto) {
        String email = registerWithFamilyIdDto.getEmail();

        if (AuthUtils.isNewUser(email)) {
            String encodedPassword = passwordEncoder.encode(registerWithFamilyIdDto.getPassword());
            registerWithFamilyIdDto.setPassword(encodedPassword);

            AuthUtils.registerUser(registerWithFamilyIdDto);
        }
    }

    public String login(LoginDto loginDto) {
        if (AuthUtils.isNewUser(loginDto.getEmail())) {
            return "";
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtGenerator.generateToken(authentication);
    }
}
