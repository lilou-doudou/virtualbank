package com.virtuallink.virtualbank.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Corps standardisé retourné par l'API lors d'une erreur.
 *
 * @param timestamp  date/heure de l'erreur
 * @param status     code HTTP
 * @param error      libellé HTTP (ex. "Not Found")
 * @param message    message métier lisible
 * @param path       URI de la requête ayant échoué
 * @param details    liste des erreurs de validation (nullable)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    /** Constructeur sans détails (erreurs non-validation). */
    public ErrorResponse(int status, String error, String message, String path) {
        this(OffsetDateTime.now(), status, error, message, path, null);
    }

    /** Constructeur avec détails (erreurs de validation). */
    public ErrorResponse(int status, String error, String message, String path, List<String> details) {
        this(OffsetDateTime.now(), status, error, message, path, details);
    }
}

