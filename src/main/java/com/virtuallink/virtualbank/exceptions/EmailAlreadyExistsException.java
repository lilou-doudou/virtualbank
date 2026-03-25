package com.virtuallink.virtualbank.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Un utilisateur avec l'email '" + email + "' existe déjà");
    }
}

