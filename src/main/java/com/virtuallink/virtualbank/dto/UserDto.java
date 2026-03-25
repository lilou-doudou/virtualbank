package com.virtuallink.virtualbank.dto;

import com.virtuallink.virtualbank.enums.UserRole;

import java.time.OffsetDateTime;

public record UserDto(
        String id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}

