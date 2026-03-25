package com.virtuallink.virtualbank.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TransferRequestDto(
        @NotBlank(message = "L'identifiant du compte destinataire est obligatoire")
        String toAccountId,
        @Positive(message = "Le montant doit être strictement positif")
        double amount,
        String description
) {}

