package com.virtuallink.virtualbank.service;

import com.virtuallink.virtualbank.dto.BankAccountDto;
import com.virtuallink.virtualbank.dto.OperationDto;
import com.virtuallink.virtualbank.requests.AmountRequestDto;
import com.virtuallink.virtualbank.requests.CreateAccountRequestDto;
import com.virtuallink.virtualbank.requests.CreateAccountsBatchRequestDto;
import com.virtuallink.virtualbank.requests.TransferRequestDto;
import com.virtuallink.virtualbank.enums.AccountStatus;
import com.virtuallink.virtualbank.enums.OperationType;
import com.virtuallink.virtualbank.exceptions.AccountNotActiveException;
import com.virtuallink.virtualbank.exceptions.BankAccountNotFoundException;
import com.virtuallink.virtualbank.exceptions.InsufficientFundsException;
import com.virtuallink.virtualbank.exceptions.InvalidOperationException;
import com.virtuallink.virtualbank.exceptions.UserNotFoundException;
import com.virtuallink.virtualbank.mapper.BankAccountMapper;
import com.virtuallink.virtualbank.mapper.OperationMapper;
import com.virtuallink.virtualbank.models.BankAccount;
import com.virtuallink.virtualbank.models.Operation;
import com.virtuallink.virtualbank.models.User;
import com.virtuallink.virtualbank.repository.BankAccountRepository;
import com.virtuallink.virtualbank.repository.OperationRepository;
import com.virtuallink.virtualbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service métier de gestion des comptes bancaires.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final UserRepository userRepository;
    private final BankAccountMapper bankAccountMapper;
    private final OperationMapper operationMapper;

    // ── Création ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public BankAccountDto createAccount(CreateAccountRequestDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));

        log.info("Création d'un compte pour l'utilisateur '{}'", user.fullName());

        BankAccount account = BankAccount.builder()
                .ownerName(user.fullName())
                .balance(request.initialBalance())
                .accountStatus(AccountStatus.ACTIVE)
                .user(user)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        BankAccount saved = accountRepository.save(account);

        if (request.initialBalance() > 0) {
            saveOperation(saved, OperationType.DEPOSIT, request.initialBalance(), "Dépôt initial");
        }

        log.info("Compte créé avec l'id '{}'", saved.getId());
        return bankAccountMapper.toDto(saved);
    }

    @Override
    @Transactional
    public List<BankAccountDto> createAccountsBatch(CreateAccountsBatchRequestDto request) {
        log.info("Creation batch de {} comptes", request.accounts().size());
        return request.accounts()
                .stream()
                .map(this::createAccount)
                .collect(Collectors.toList());
    }

    // ── Consultation ───────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public BankAccountDto getAccount(String accountId) {
        return bankAccountMapper.toDto(findAccountOrThrow(accountId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccountDto> getAllAccounts() {
        return bankAccountMapper.toDtoList(accountRepository.findAll());
    }

    // ── Dépôt ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public BankAccountDto deposit(String accountId, AmountRequestDto request) {
        BankAccount account = findActiveAccountOrThrow(accountId);

        account.setBalance(account.getBalance() + request.amount());
        account.setUpdatedAt(OffsetDateTime.now());

        saveOperation(account, OperationType.DEPOSIT, request.amount(),
                request.description() != null ? request.description() : "Dépôt");

        log.info("Dépôt de {} sur le compte '{}'", request.amount(), accountId);
        return bankAccountMapper.toDto(accountRepository.save(account));
    }

    // ── Retrait ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public BankAccountDto withdraw(String accountId, AmountRequestDto request) {
        BankAccount account = findActiveAccountOrThrow(accountId);

        if (account.getBalance() < request.amount()) {
            throw new InsufficientFundsException(accountId, account.getBalance(), request.amount());
        }

        account.setBalance(account.getBalance() - request.amount());
        account.setUpdatedAt(OffsetDateTime.now());

        saveOperation(account, OperationType.WITHDRAWAL, request.amount(),
                request.description() != null ? request.description() : "Retrait");

        log.info("Retrait de {} sur le compte '{}'", request.amount(), accountId);
        return bankAccountMapper.toDto(accountRepository.save(account));
    }

    // ── Virement ──────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public BankAccountDto transfer(String fromAccountId, TransferRequestDto request) {
        if (fromAccountId.equals(request.toAccountId())) {
            throw new InvalidOperationException("Le compte source et le compte destinataire sont identiques");
        }

        BankAccount source = findActiveAccountOrThrow(fromAccountId);
        BankAccount target = findActiveAccountOrThrow(request.toAccountId());

        if (source.getBalance() < request.amount()) {
            throw new InsufficientFundsException(fromAccountId, source.getBalance(), request.amount());
        }

        String label = request.description() != null ? request.description() : "Virement";

        source.setBalance(source.getBalance() - request.amount());
        source.setUpdatedAt(OffsetDateTime.now());
        saveOperation(source, OperationType.TRANSFER_DEBIT, request.amount(),
                label + " → " + target.getOwnerName());

        target.setBalance(target.getBalance() + request.amount());
        target.setUpdatedAt(OffsetDateTime.now());
        saveOperation(target, OperationType.TRANSFER_CREDIT, request.amount(),
                label + " ← " + source.getOwnerName());

        accountRepository.save(target);

        log.info("Virement de {} de '{}' vers '{}'", request.amount(), fromAccountId, request.toAccountId());
        return bankAccountMapper.toDto(accountRepository.save(source));
    }

    // ── Opérations ────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<OperationDto> getOperations(String accountId) {
        findAccountOrThrow(accountId); // vérifie l'existence du compte
        return operationMapper.toDtoList(
                operationRepository.findAllByBankAccountIdOrderByOperationDateDesc(accountId));
    }

    // ── Fermeture ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public BankAccountDto closeAccount(String accountId) {
        BankAccount account = findAccountOrThrow(accountId);

        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new InvalidOperationException("Le compte '" + accountId + "' est déjà fermé");
        }

        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(OffsetDateTime.now());
        account.setUpdatedAt(OffsetDateTime.now());

        log.info("Fermeture du compte '{}'", accountId);
        return bankAccountMapper.toDto(accountRepository.save(account));
    }

    // ── Helpers privés ────────────────────────────────────────────────────────

    private BankAccount findAccountOrThrow(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException(accountId));
    }

    private BankAccount findActiveAccountOrThrow(String accountId) {
        BankAccount account = findAccountOrThrow(accountId);
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(accountId, account.getAccountStatus());
        }
        return account;
    }

    private void saveOperation(BankAccount account, OperationType type,
                                double amount, String description) {
        Operation operation = Operation.builder()
                .type(type)
                .amount(amount)
                .description(description)
                .operationDate(OffsetDateTime.now())
                .bankAccount(account)
                .build();
        operationRepository.save(operation);
    }
}

