package com.virtuallink.virtualbank.controllers;

import com.virtuallink.virtualbank.dto.BankAccountDto;
import com.virtuallink.virtualbank.dto.OperationDto;
import com.virtuallink.virtualbank.requests.AmountRequestDto;
import com.virtuallink.virtualbank.requests.CreateAccountRequestDto;
import com.virtuallink.virtualbank.requests.TransferRequestDto;
import com.virtuallink.virtualbank.exceptions.ErrorResponse;
import com.virtuallink.virtualbank.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des comptes bancaires.
 * Base URL : /api/v1/accounts
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Comptes bancaires", description = "Opérations CRUD et transactions sur les comptes")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    // ── POST /api/v1/accounts ─────────────────────────────────────────────────

    @Operation(summary = "Créer un compte", description = "Crée un nouveau compte bancaire avec un solde initial optionnel.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Compte créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BankAccountDto> createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountService.createAccount(request));
    }

    // ── GET /api/v1/accounts ──────────────────────────────────────────────────

    @Operation(summary = "Lister tous les comptes", description = "Retourne la liste complète des comptes bancaires.")
    @ApiResponse(responseCode = "200", description = "Liste des comptes")
    @GetMapping
    public ResponseEntity<List<BankAccountDto>> getAllAccounts() {
        return ResponseEntity.ok(bankAccountService.getAllAccounts());
    }

    // ── GET /api/v1/accounts/{id} ─────────────────────────────────────────────

    @Operation(summary = "Consulter un compte", description = "Retourne les détails d'un compte par son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compte trouvé"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDto> getAccount(
            @Parameter(description = "Identifiant UUID du compte") @PathVariable String id) {
        return ResponseEntity.ok(bankAccountService.getAccount(id));
    }

    // ── POST /api/v1/accounts/{id}/deposit ────────────────────────────────────

    @Operation(summary = "Effectuer un dépôt", description = "Crédite le montant indiqué sur le compte.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dépôt effectué"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Compte non actif",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/deposit")
    public ResponseEntity<BankAccountDto> deposit(
            @Parameter(description = "Identifiant UUID du compte") @PathVariable String id,
            @Valid @RequestBody AmountRequestDto request) {
        return ResponseEntity.ok(bankAccountService.deposit(id, request));
    }

    // ── POST /api/v1/accounts/{id}/withdraw ───────────────────────────────────

    @Operation(summary = "Effectuer un retrait", description = "Débite le montant indiqué du compte.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrait effectué"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Compte non actif",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Solde insuffisant",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BankAccountDto> withdraw(
            @Parameter(description = "Identifiant UUID du compte") @PathVariable String id,
            @Valid @RequestBody AmountRequestDto request) {
        return ResponseEntity.ok(bankAccountService.withdraw(id, request));
    }

    // ── POST /api/v1/accounts/{id}/transfer ───────────────────────────────────

    @Operation(summary = "Effectuer un virement", description = "Transfère un montant du compte source vers un compte destinataire.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Virement effectué"),
            @ApiResponse(responseCode = "404", description = "Compte source ou destinataire introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Compte non actif",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Solde insuffisant ou opération invalide",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/transfer")
    public ResponseEntity<BankAccountDto> transfer(
            @Parameter(description = "Identifiant UUID du compte source") @PathVariable String id,
            @Valid @RequestBody TransferRequestDto request) {
        return ResponseEntity.ok(bankAccountService.transfer(id, request));
    }

    // ── GET /api/v1/accounts/{id}/operations ──────────────────────────────────

    @Operation(summary = "Historique des opérations", description = "Retourne les opérations d'un compte, triées par date décroissante.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des opérations"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/operations")
    public ResponseEntity<List<OperationDto>> getOperations(
            @Parameter(description = "Identifiant UUID du compte") @PathVariable String id) {
        return ResponseEntity.ok(bankAccountService.getOperations(id));
    }

    // ── DELETE /api/v1/accounts/{id} ──────────────────────────────────────────

    @Operation(summary = "Fermer un compte", description = "Passe le compte au statut CLOSED. Opération irréversible.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compte fermé"),
            @ApiResponse(responseCode = "404", description = "Compte introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Compte déjà fermé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BankAccountDto> closeAccount(
            @Parameter(description = "Identifiant UUID du compte") @PathVariable String id) {
        return ResponseEntity.ok(bankAccountService.closeAccount(id));
    }
}

