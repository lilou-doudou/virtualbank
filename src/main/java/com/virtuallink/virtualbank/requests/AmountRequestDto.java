package com.virtuallink.virtualbank.requests;

import jakarta.validation.constraints.Positive;

public record AmountRequestDto(
        @Positive(message = "Le montant doit être strictement positif")
        double amount,
        String description
) {}
