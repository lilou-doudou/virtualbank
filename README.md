# VirtualBank - API de Gestion de Comptes Bancaires

## 📋 Idée du Projet

VirtualBank est une API Spring Boot complète pour la gestion de comptes bancaires, associée à un agent IA générateur de tests unitaires pour les services Spring. Ce projet démontre les meilleures pratiques modernes en développement Java/Spring.

### Objectifs

1. **API Spring Boot de gestion de comptes bancaires** - Fournir une API REST robuste et bien structurée
2. **Agent IA générateur de tests unitaires** - Automatiser la génération de tests pour les services Spring

---

## 🎯 Fonctionnalités

### Gestion des Comptes

- ✅ **Création d'un compte bancaire** - Initialiser un nouveau compte avec les informations de base
- ✅ **Consultation d'un compte** - Accéder aux détails et au solde d'un compte
- ✅ **Dépôt** - Ajouter des fonds à un compte
- ✅ **Retrait** - Retirer des fonds d'un compte
- ✅ **Virement entre comptes** - Effectuer des transferts entre deux comptes
- ✅ **Liste des opérations** - Consulter l'historique complet des transactions

---

## 🛠️ Stack Technique

| Composant | Version | Description |
|-----------|---------|-------------|
| **Java** | 21 | Langage de programmation |
| **Spring Boot** | 3.4.x | Framework d'application |
| **Spring Web** | 3.4.x | REST API et contrôleurs |
| **Spring Data JPA** | 3.4.x | Accès aux données et persistance |
| **H2 Database** | Latest | Base de données en mémoire (développement) |
| **Lombok** | Latest | Réduction du boilerplate code |
| **Validation** | 3.4.x | Validation des données |
| **JUnit 5** | Latest | Framework de tests unitaires |
| **Mockito** | Latest | Mock et spy pour les tests |

---

## 📦 Structure du Projet

```
virtualbank/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/virtuallink/virtualbank/
│   │   │       ├── VirtualbankApplication.java
│   │   │       ├── controller/          # Contrôleurs REST
│   │   │       ├── service/             # Logique métier
│   │   │       ├── repository/          # Accès aux données
│   │   │       ├── entity/              # Entités JPA
│   │   │       ├── dto/                 # Data Transfer Objects
│   │   │       ├── exception/           # Gestion des exceptions
│   │   │       └── config/              # Configuration
│   │   └── resources/
│   │       ├── application.yaml         # Configuration Spring
│   │       └── data.sql                 # Données initiales
│   └── test/
│       └── java/com/virtuallink/virtualbank/
│           └── ...                      # Tests unitaires
├── pom.xml
└── README.md
```

---

## 🚀 Démarrage Rapide

### Prérequis

- JDK 21 ou supérieur
- Maven 3.8.x ou supérieur

### Installation et Exécution

```bash
# Cloner le projet
git clone <repository-url>
cd virtualbank

# Compiler le projet
./mvnw clean compile

# Lancer les tests
./mvnw test

# Démarrer l'application
./mvnw spring-boot:run
```

L'API sera disponible à `http://localhost:8080`

---

## 📝 Endpoints API (Exemple)

### Comptes

```
POST   /api/accounts              # Créer un compte
GET    /api/accounts/{id}         # Consulter un compte
GET    /api/accounts              # Lister tous les comptes
```

### Opérations

```
POST   /api/accounts/{id}/deposit # Effectuer un dépôt
POST   /api/accounts/{id}/withdraw # Effectuer un retrait
POST   /api/accounts/{from}/transfer # Virement entre comptes
GET    /api/accounts/{id}/operations # Historique des opérations
```

---

## 🧪 Tests Unitaires

Ce projet intègre un agent IA pour la génération automatisée de tests unitaires. Les tests sont structurés avec :

- **JUnit 5** pour le framework de tests
- **Mockito** pour les mocks et spies
- **AssertJ** pour les assertions fluides (optionnel)

### Lancer les tests

```bash
./mvnw test
```

### Générer des tests avec l'agent IA

*Documentation de l'agent IA à venir*

---

## 🏗️ Architecture

L'application suit une architecture en couches classique :

- **Controller Layer** : Gestion des requêtes HTTP
- **Service Layer** : Logique métier et orchestration
- **Repository Layer** : Accès aux données (Spring Data JPA)
- **Entity Layer** : Modèle de données persistant
- **DTO Layer** : Transformation des données pour l'API

---

## 💾 Persistance des Données

L'application utilise **H2 Database** pour le stockage en mémoire lors du développement. Cela permet un démarrage rapide sans configuration externe.

Pour la production, vous pouvez configurer une base de données relationnelle (PostgreSQL, MySQL, etc.) dans `application.yaml`.

---

## 🔒 Validation

Tous les inputs sont validés à l'aide de :

- **Jakarta Validation API**
- **Contraintes personnalisées** pour les règles métier spécifiques

Exemple :
- Montants positifs pour les dépôts/retraits
- Solde suffisant pour les retraits
- Identifiants de comptes valides

---

## 📚 Technologies et Dépendances

Pour une liste complète des dépendances, consultez `pom.xml`.

### Principales dépendances

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

## 📄 Licence

Ce projet est fourni à titre d'exemple éducatif.

---

## 📞 Support

Pour toute question ou suggestion concernant ce projet, veuillez consulter la documentation ou créer une issue.

---

**Dernière mise à jour** : Mars 2026
**Version** : 1.0.0-SNAPSHOT

