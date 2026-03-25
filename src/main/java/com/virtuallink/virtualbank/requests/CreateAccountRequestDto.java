package com.virtuallink.virtualbank.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateAccountRequestDto(
        @NotBlank(message = "L'identifiant de l'utilisateur est obligatoire")
        String userId,
        @PositiveOrZero(message = "Le solde initial doit être positif ou nul")
        double initialBalance
) {}
