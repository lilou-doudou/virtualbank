package com.virtuallink.virtualbank.exceptions;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(String accountId) {
        super("Compte bancaire introuvable avec l'identifiant : " + accountId);
    }
}

