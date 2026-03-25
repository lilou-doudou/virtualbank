package com.virtuallink.virtualbank.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateAccountsBatchRequestDto(
        @NotEmpty(message = "La liste des comptes a creer ne peut pas etre vide")
        @Size(max = 100, message = "La creation batch est limitee a 100 comptes par requete")
        List<@Valid CreateAccountRequestDto> accounts
) {}

