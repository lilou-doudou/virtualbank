<<<<<<< HEAD
# VirtualBank — API de Gestion de Comptes Bancaires

## 📋 Idée du Projet

VirtualBank est une API Spring Boot complète pour la gestion d'utilisateurs et de comptes bancaires, associée à un agent IA générateur de tests unitaires pour les services Spring.

### Objectifs

1. **API Spring Boot de gestion bancaire** — Fournir une API REST robuste et bien structurée
2. **Agent IA générateur de tests unitaires** — Automatiser la génération de tests pour les services Spring

---

## 🎯 Fonctionnalités

### 👤 Gestion des Utilisateurs
- ✅ **Création d'un utilisateur** — Prénom, nom, email unique, mot de passe, rôle USER/ADMIN
- ✅ **Consultation** — Détails d'un profil
- ✅ **Mise à jour** — Modifier prénom, nom ou email
- ✅ **Suppression** — Supprime l'utilisateur et tous ses comptes
- ✅ **Comptes d'un utilisateur** — Lister tous les comptes rattachés

### 🏦 Gestion des Comptes Bancaires
- ✅ **Création d'un compte** — Rattaché à un utilisateur existant
- ✅ **Création batch** — Jusqu'à 100 comptes en une seule requête
- ✅ **Consultation** — Détails, solde avec devise €, statut
- ✅ **Dépôt / Retrait** — Avec vérification du solde
- ✅ **Virement** — Entre deux comptes actifs
- ✅ **Historique des opérations** — Trié par date décroissante
- ✅ **Fermeture** — Passage au statut CLOSED (irréversible)

---

## 🛠️ Stack Technique

| Composant | Version | Description |
|---|---|---|
| **Java** | 22 | Langage de programmation |
| **Spring Boot** | 3.4.3 | Framework d'application |
| **Spring Data JPA** | — | Accès aux données |
| **H2 Database** | — | Base en mémoire (dev) |
| **Lombok** | 1.18.38 | Réduction du boilerplate |
| **MapStruct** | 1.6.3 | Mapping entités ↔ DTOs |
| **Springdoc OpenAPI** | 2.8.5 | Documentation Swagger UI |
| **JUnit 5 + MockMvc** | — | Tests d'intégration |

---

## 📦 Structure du Projet

```
virtualbank/
├── src/main/java/com/virtuallink/virtualbank/
│   ├── config/                    # OpenApiConfig
│   ├── controllers/
│   │   ├── UserController.java
│   │   └── BankAccountController.java
│   ├── service/
│   │   ├── UserService.java / UserServiceImpl.java
│   │   └── BankAccountService.java / BankAccountServiceImpl.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── BankAccountRepository.java
│   │   └── OperationRepository.java
│   ├── models/
│   │   ├── User.java
│   │   ├── BankAccount.java
│   │   └── Operation.java
│   ├── dto/                       # Réponses API (Java records)
│   │   ├── UserDto.java
│   │   ├── BankAccountDto.java
│   │   └── OperationDto.java
│   ├── requests/                  # Corps de requêtes (Java records)
│   │   ├── CreateUserRequestDto.java
│   │   ├── UpdateUserRequestDto.java
│   │   ├── CreateAccountRequestDto.java
│   │   ├── CreateAccountsBatchRequestDto.java
│   │   ├── AmountRequestDto.java
│   │   └── TransferRequestDto.java
│   ├── mapper/
│   │   ├── UserMapper.java
│   │   ├── BankAccountMapper.java
│   │   └── OperationMapper.java
│   ├── enums/
│   │   ├── UserRole.java          # USER | ADMIN
│   │   ├── AccountStatus.java     # ACTIVE | INACTIVE | SUSPENDED | CLOSED
│   │   └── OperationType.java     # DEPOSIT | WITHDRAWAL | TRANSFER_*
│   └── exceptions/
│       ├── GlobalExceptionHandler.java
│       ├── ErrorResponse.java
│       ├── UserNotFoundException.java
│       ├── EmailAlreadyExistsException.java
│       ├── BankAccountNotFoundException.java
│       ├── InsufficientFundsException.java
│       ├── AccountNotActiveException.java
│       └── InvalidOperationException.java
├── requests/                      # Fichiers .http IntelliJ
│   ├── accounts.http
│   └── transactions.http
└── pom.xml
```

---

## 🚀 Démarrage Rapide

```bash
./mvnw clean compile     # Compiler
./mvnw test              # Tests
./mvnw spring-boot:run   # Démarrer
```

---

## 🌐 URLs utiles

| URL | Description |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Swagger UI |
| `http://localhost:8080/v3/api-docs` | Spec OpenAPI JSON |
| `http://localhost:8080/h2-console` | Console H2 |

**Connexion H2** : JDBC URL `jdbc:h2:mem:virtualbankdb` — User `sa` — Password *(vide)*

---

## 📝 Endpoints API

### 👤 Utilisateurs — `/api/v1/users`

| Méthode | URL | Description | Code |
|---|---|---|---|
| `POST` | `/api/v1/users` | Créer un utilisateur | 201 |
| `GET` | `/api/v1/users` | Lister tous les utilisateurs | 200 |
| `GET` | `/api/v1/users/{id}` | Consulter un utilisateur | 200 |
| `PUT` | `/api/v1/users/{id}` | Mettre à jour | 200 |
| `DELETE` | `/api/v1/users/{id}` | Supprimer | 204 |
| `GET` | `/api/v1/users/{id}/accounts` | Comptes de l'utilisateur | 200 |

### 🏦 Comptes — `/api/v1/accounts`

| Méthode | URL | Description | Code |
|---|---|---|---|
| `POST` | `/api/v1/accounts` | Créer un compte | 201 |
| `POST` | `/api/v1/accounts/batch` | Créer plusieurs comptes | 201 |
| `GET` | `/api/v1/accounts` | Lister tous les comptes | 200 |
| `GET` | `/api/v1/accounts/{id}` | Consulter un compte | 200 |
| `POST` | `/api/v1/accounts/{id}/deposit` | Dépôt | 200 |
| `POST` | `/api/v1/accounts/{id}/withdraw` | Retrait | 200 |
| `POST` | `/api/v1/accounts/{id}/transfer` | Virement | 200 |
| `GET` | `/api/v1/accounts/{id}/operations` | Historique | 200 |
| `DELETE` | `/api/v1/accounts/{id}` | Fermer un compte | 200 |

### 🔄 Flux complet

```json
// 1. Créer un utilisateur
POST /api/v1/users
{ "firstName": "Alice", "lastName": "Dupont", "email": "alice@test.com", "password": "secret123" }

// 2. Créer un compte pour cet utilisateur
POST /api/v1/accounts
{ "userId": "<id-retourné>", "initialBalance": 1500.0 }

// 3. Dépôt
POST /api/v1/accounts/<account-id>/deposit
{ "amount": 250.0, "description": "Salaire" }

// 4. Virement
POST /api/v1/accounts/<account-id>/transfer
{ "toAccountId": "<autre-id>", "amount": 100.0, "description": "Loyer" }
```

---

## 📐 Modèle de données

```
User (bank_users)
 ├── id          UUID
 ├── firstName   String
 ├── lastName    String
 ├── email       String (unique)
 ├── password    String
 ├── role        UserRole (USER | ADMIN)
 ├── createdAt / updatedAt
 └── accounts    OneToMany → BankAccount

BankAccount (bank_accounts)
 ├── id            UUID
 ├── ownerName     String (= user.firstName + user.lastName)
 ├── balance       double  (+balanceDisplay "1500.00 €")
 ├── accountStatus AccountStatus
 ├── createdAt / updatedAt
 ├── closedAt      OffsetDateTime (absent du JSON si null)
 ├── user          ManyToOne → User
 └── operations    OneToMany → Operation

Operation (operations)
 ├── id            UUID
 ├── type          OperationType
 ├── amount        double
 ├── description   String
 ├── operationDate OffsetDateTime
 └── bankAccount   ManyToOne → BankAccount
```

---

## 🛡️ Gestion des Erreurs

```json
{
  "timestamp": "2026-03-25T14:00:00+01:00",
  "status": 404,
  "error": "Not Found",
  "message": "Utilisateur introuvable avec l'identifiant : abc-123",
  "path": "/api/v1/users/abc-123"
}
```

| Code | Cas d'usage |
|---|---|
| `400` | Données invalides (validation) |
| `404` | Utilisateur ou compte introuvable |
| `409` | Email déjà existant, compte non actif |
| `422` | Solde insuffisant, opération invalide |
| `500` | Erreur interne |

---

## 🧪 Tests

```bash
./mvnw test
```

**4 tests d'intégration MockMvc + H2 :**

| Test | Ce qui est vérifié |
|---|---|
| `closedAt` absent si compte ouvert | Sérialisation JSON conditionnelle |
| `closedAt` présent après fermeture | Contrat JSON post-fermeture |
| Création batch | Plusieurs comptes + `closedAt` absent |
| Comptes d'un utilisateur | Liaison User ↔ BankAccount |
| Email dupliqué → 409 | Unicité de l'email |

---

## 🤖 Agent IA — Génération de Tests

*Prochaine étape du projet.*

L'agent analysera les services Spring et génèrera automatiquement les classes de test JUnit 5 avec mocks Mockito, cas nominaux et cas d'erreur.

---

**Version** : 1.1.0-SNAPSHOT — **Dernière mise à jour** : Mars 2026
=======
# virtualbank
VirtualBank est une API Spring Boot complète pour la gestion d'utilisateurs et de comptes bancaires, associée à un agent IA générateur de tests unitaires pour les services Spring.
>>>>>>> origin/main
