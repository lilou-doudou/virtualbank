package com.virtuallink.virtualbank.dto;

import com.virtuallink.virtualbank.enums.AccountStatus;

import java.time.OffsetDateTime;

public record BankAccountDto(
        String id,
        String ownerName,
        double balance,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime closedAt,
        AccountStatus accountStatus
) {}
