package com.mobylab.springbackend.service.dto;

import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisterWithFamilyIdRequestDto {
    private UUID familyId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Pattern(regexp = "PARENT|CHILD")
    private String role;

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
