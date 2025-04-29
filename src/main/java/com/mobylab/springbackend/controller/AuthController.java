package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.AuthService;
import com.mobylab.springbackend.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private LoginResponseDto loginResponseDto;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(path ="/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        logger.info("Request to register user {}", registerRequestDto.getEmail());
        UUID familyId = authService.registerWithoutFamilyId(registerRequestDto);
        logger.info("Successfully registered user {}", registerRequestDto.getEmail());

        return new ResponseEntity<>(familyId, HttpStatus.OK);
    }

    @RequestMapping(path ="/register/family-id", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterWithFamilyIdRequestDto registerDto) {
        logger.info("Request to register user {}", registerDto.getEmail());
        authService.registerWithFamilyId(registerDto);
        logger.info("Successfully registered user {}", registerDto.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path ="/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        logger.info("Request to login for user {}", loginRequestDto.getEmail());
        String token = authService.login(loginRequestDto);
        logger.info("Successfully logged in user {}", loginRequestDto.getEmail());

        loginResponseDto.setToken(token);

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }
}
