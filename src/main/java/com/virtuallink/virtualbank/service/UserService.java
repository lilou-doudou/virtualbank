package com.virtuallink.virtualbank.service;

import com.virtuallink.virtualbank.dto.BankAccountDto;
import com.virtuallink.virtualbank.dto.UserDto;
import com.virtuallink.virtualbank.requests.CreateUserRequestDto;
import com.virtuallink.virtualbank.requests.UpdateUserRequestDto;

import java.util.List;

/**
 * Contrat du service métier de gestion des utilisateurs.
 */
public interface UserService {

    /** Crée un nouvel utilisateur. */
    UserDto createUser(CreateUserRequestDto request);

    /** Retourne le détail d'un utilisateur par son identifiant. */
    UserDto getUser(String userId);

    /** Retourne tous les utilisateurs. */
    List<UserDto> getAllUsers();

    /** Met à jour les informations d'un utilisateur. */
    UserDto updateUser(String userId, UpdateUserRequestDto request);

    /** Supprime un utilisateur (et ses comptes associés). */
    void deleteUser(String userId);

    /** Retourne tous les comptes bancaires d'un utilisateur. */
    List<BankAccountDto> getUserAccounts(String userId);
}

