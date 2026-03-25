package com.virtuallink.virtualbank.controllers;

import com.virtuallink.virtualbank.dto.BankAccountDto;
import com.virtuallink.virtualbank.dto.UserDto;
import com.virtuallink.virtualbank.exceptions.ErrorResponse;
import com.virtuallink.virtualbank.requests.CreateUserRequestDto;
import com.virtuallink.virtualbank.requests.UpdateUserRequestDto;
import com.virtuallink.virtualbank.service.UserService;
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
 * Contrôleur REST pour la gestion des utilisateurs.
 * Base URL : /api/v1/users
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "CRUD des utilisateurs et consultation de leurs comptes")
public class UserController {

    private final UserService userService;

    // ── POST /api/v1/users ────────────────────────────────────────────────────

    @Operation(summary = "Créer un utilisateur", description = "Crée un nouvel utilisateur avec le rôle USER par défaut.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email déjà utilisé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    // ── GET /api/v1/users ─────────────────────────────────────────────────────

    @Operation(summary = "Lister tous les utilisateurs")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ── GET /api/v1/users/{id} ────────────────────────────────────────────────

    @Operation(summary = "Consulter un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @Parameter(description = "Identifiant UUID de l'utilisateur") @PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    // ── PUT /api/v1/users/{id} ────────────────────────────────────────────────

    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour les champs non nuls fournis.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email déjà utilisé",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "Identifiant UUID de l'utilisateur") @PathVariable String id,
            @Valid @RequestBody UpdateUserRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // ── DELETE /api/v1/users/{id} ─────────────────────────────────────────────

    @Operation(summary = "Supprimer un utilisateur", description = "Supprime l'utilisateur et tous ses comptes associés.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Identifiant UUID de l'utilisateur") @PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ── GET /api/v1/users/{id}/accounts ──────────────────────────────────────

    @Operation(summary = "Comptes d'un utilisateur", description = "Retourne tous les comptes bancaires d'un utilisateur.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des comptes"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<BankAccountDto>> getUserAccounts(
            @Parameter(description = "Identifiant UUID de l'utilisateur") @PathVariable String id) {
        return ResponseEntity.ok(userService.getUserAccounts(id));
    }
}

