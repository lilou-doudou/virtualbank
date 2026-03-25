package com.virtuallink.virtualbank.service;

import com.virtuallink.virtualbank.dto.BankAccountDto;
import com.virtuallink.virtualbank.dto.UserDto;
import com.virtuallink.virtualbank.enums.UserRole;
import com.virtuallink.virtualbank.exceptions.EmailAlreadyExistsException;
import com.virtuallink.virtualbank.exceptions.UserNotFoundException;
import com.virtuallink.virtualbank.mapper.BankAccountMapper;
import com.virtuallink.virtualbank.mapper.UserMapper;
import com.virtuallink.virtualbank.models.User;
import com.virtuallink.virtualbank.repository.BankAccountRepository;
import com.virtuallink.virtualbank.repository.UserRepository;
import com.virtuallink.virtualbank.requests.CreateUserRequestDto;
import com.virtuallink.virtualbank.requests.UpdateUserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Implémentation du service métier de gestion des utilisateurs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserMapper userMapper;
    private final BankAccountMapper bankAccountMapper;

    // ── Création ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequestDto request) {
        log.info("Création d'un utilisateur pour '{} {}'", request.firstName(), request.lastName());

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = userMapper.toEntity(request);
        user.setRole(UserRole.USER);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        User saved = userRepository.save(user);
        log.info("Utilisateur créé avec l'id '{}'", saved.getId());
        return userMapper.toDto(saved);
    }

    // ── Consultation ───────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(String userId) {
        return userMapper.toDto(findUserOrThrow(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    // ── Mise à jour ───────────────────────────────────────────────────────────

    @Override
    @Transactional
    public UserDto updateUser(String userId, UpdateUserRequestDto request) {
        User user = findUserOrThrow(userId);

        if (request.email() != null
                && !request.email().equals(user.getEmail())
                && userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName()  != null) user.setLastName(request.lastName());
        if (request.email()     != null) user.setEmail(request.email());
        user.setUpdatedAt(OffsetDateTime.now());

        log.info("Mise à jour de l'utilisateur '{}'", userId);
        return userMapper.toDto(userRepository.save(user));
    }

    // ── Suppression ───────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = findUserOrThrow(userId);
        userRepository.delete(user);
        log.info("Utilisateur '{}' supprimé", userId);
    }

    // ── Comptes de l'utilisateur ──────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<BankAccountDto> getUserAccounts(String userId) {
        findUserOrThrow(userId);
        return bankAccountMapper.toDtoList(
                bankAccountRepository.findAllByUserId(userId));
    }

    // ── Helper privé ──────────────────────────────────────────────────────────

    private User findUserOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}

