package com.virtuallink.virtualbank.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("Utilisateur introuvable avec l'identifiant : " + userId);
    }
}

