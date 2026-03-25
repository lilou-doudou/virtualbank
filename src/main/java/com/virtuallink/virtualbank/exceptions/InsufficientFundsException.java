package com.virtuallink.virtualbank.exceptions;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String accountId, double balance, double amount) {
        super(String.format(
                "Solde insuffisant sur le compte %s : solde disponible %.2f, montant demandé %.2f",
                accountId, balance, amount
        ));
    }
}

