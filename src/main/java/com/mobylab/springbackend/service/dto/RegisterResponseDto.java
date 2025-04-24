package com.mobylab.springbackend.service.dto;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisterResponseDto {
    private UUID familyId;

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }
}
