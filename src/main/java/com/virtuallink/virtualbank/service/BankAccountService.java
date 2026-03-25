package com.virtuallink.virtualbank.service;

import com.virtuallink.virtualbank.dto.BankAccountDto;
import com.virtuallink.virtualbank.dto.OperationDto;
import com.virtuallink.virtualbank.requests.AmountRequestDto;
import com.virtuallink.virtualbank.requests.CreateAccountRequestDto;
import com.virtuallink.virtualbank.requests.CreateAccountsBatchRequestDto;
import com.virtuallink.virtualbank.requests.TransferRequestDto;

import java.util.List;

/**
 * Contrat du service métier de gestion des comptes bancaires.
 */
public interface BankAccountService {

    /** Crée un nouveau compte bancaire. */
    BankAccountDto createAccount(CreateAccountRequestDto request);

    /** Crée plusieurs comptes bancaires en une seule requête. */
    List<BankAccountDto> createAccountsBatch(CreateAccountsBatchRequestDto request);

    /** Retourne le détail d'un compte par son identifiant. */
    BankAccountDto getAccount(String accountId);

    /** Retourne tous les comptes bancaires. */
    List<BankAccountDto> getAllAccounts();

    /** Effectue un dépôt sur un compte. */
    BankAccountDto deposit(String accountId, AmountRequestDto request);

    /** Effectue un retrait sur un compte. */
    BankAccountDto withdraw(String accountId, AmountRequestDto request);

    /** Effectue un virement d'un compte vers un autre. */
    BankAccountDto transfer(String fromAccountId, TransferRequestDto request);

    /** Retourne l'historique des opérations d'un compte. */
    List<OperationDto> getOperations(String accountId);

    /** Ferme un compte (passage au statut CLOSED). */
    BankAccountDto closeAccount(String accountId);
}

