package com.virtuallink.virtualbank.repository;

import com.virtuallink.virtualbank.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {

    /** Retourne toutes les opérations d'un compte, triées par date décroissante. */
    List<Operation> findAllByBankAccountIdOrderByOperationDateDesc(String bankAccountId);
}

