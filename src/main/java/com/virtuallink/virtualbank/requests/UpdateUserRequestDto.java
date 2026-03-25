package com.virtuallink.virtualbank.requests;

import jakarta.validation.constraints.Email;

public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        @Email(message = "L'email doit être valide")
        String email
) {}

