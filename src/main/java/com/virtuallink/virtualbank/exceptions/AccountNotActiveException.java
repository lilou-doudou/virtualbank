package com.virtuallink.virtualbank.exceptions;

import com.virtuallink.virtualbank.enums.AccountStatus;

public class AccountNotActiveException extends RuntimeException {

    public AccountNotActiveException(String accountId, AccountStatus status) {
        super(String.format(
                "L'opération est impossible : le compte %s est dans l'état '%s'",
                accountId, status
        ));
    }
}

