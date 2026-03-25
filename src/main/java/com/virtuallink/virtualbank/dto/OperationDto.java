package com.virtuallink.virtualbank.dto;

import com.virtuallink.virtualbank.enums.OperationType;

import java.time.OffsetDateTime;

public record OperationDto(
        String id,
        OperationType type,
        double amount,
        String description,
        OffsetDateTime operationDate,
        String bankAccountId
) {}
