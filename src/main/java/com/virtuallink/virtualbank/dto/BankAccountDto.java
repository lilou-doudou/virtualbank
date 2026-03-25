package com.virtuallink.virtualbank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.virtuallink.virtualbank.enums.AccountStatus;

import java.time.OffsetDateTime;
import java.util.Locale;

public record BankAccountDto(
        String id,
        String userId,
        String ownerName,
        double balance,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        OffsetDateTime closedAt,
        AccountStatus accountStatus
) {
    /** Champ d'affichage formaté avec la devise euro. */
    public String balanceDisplay() {
        return String.format(Locale.ROOT, "%.2f €", balance);
    }
}
