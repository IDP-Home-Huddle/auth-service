package com.mobylab.springbackend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobylab.springbackend.exception.AuthException;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.service.dto.RegisterDto;
import com.mobylab.springbackend.service.dto.RegisterWithFamilyIdDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;

public final class AuthUtils {
    private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);

    private static final HttpClient client = HttpClient.newHttpClient();

    public static boolean isNewUser(String email) {
        String url = "http://localhost:8083/database/check-register";

        String json = """
            {
              "email": "%s"
            }
            """.formatted(email);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return true;
            } else {
                System.out.println("register - isRegistered - user already registered");
                logger.error("register - isRegistered - user already registered");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("register - isRegistered error");
            logger.error("register - isRegistered error");
        }

        return false;
    }

    public static UUID registerUser(RegisterWithFamilyIdDto registerWithFamilyIdDto) {
        String url = "http://localhost:8083/database/register-user";

        String json = """
            {
                "familyId": "%s",
                "firstName": "%s",
                "lastName": "%s",
                "email": "%s",
                "password": "%s",
                "role": "%s"
            }
            """.formatted(
                    registerWithFamilyIdDto.getFamilyId(),
                registerWithFamilyIdDto.getFirstName(),
                registerWithFamilyIdDto.getLastName(),
                registerWithFamilyIdDto.getEmail(),
                registerWithFamilyIdDto.getPassword(),
                registerWithFamilyIdDto.getRole()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(response.body());

                return  UUID.fromString(responseJson.get("familyId").asText());
            } else {
                System.out.println("register - registerUser - register failed");
                logger.error("register - registerUser - register failed");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("register - registerUser error");
            logger.error("register - registerUser error");
        }

        return null;
    }
}