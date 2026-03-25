package com.virtuallink.virtualbank.repository;

import com.virtuallink.virtualbank.enums.AccountStatus;
import com.virtuallink.virtualbank.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    /** Recherche tous les comptes ayant un statut donné. */
    List<BankAccount> findAllByAccountStatus(AccountStatus status);

    /** Recherche tous les comptes appartenant à un utilisateur. */
    List<BankAccount> findAllByUserId(String userId);

    /** Vérifie l'existence d'un compte par nom du titulaire. */
    boolean existsByOwnerName(String ownerName);
}

