# Module de Facturation — API REST Spring Boot

API REST de gestion de **clients** et de **factures** : création de factures multi-lignes, calcul automatique des totaux HT / TVA / TTC, validation métier et export JSON. L'API est documentée avec Swagger.

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)
![H2](https://img.shields.io/badge/DB-H2-blue)
![OpenAPI](https://img.shields.io/badge/Docs-OpenAPI%203-85EA2D)

## Fonctionnalités

- **Clients** : CRUD, recherche par nom, contrôle d'unicité de l'email et du SIRET
- **Factures** : CRUD avec lignes de facture (cascade JPA, `orphanRemoval`)
- **Calculs automatiques** des totaux HT, TVA et TTC par ligne et par facture (`BigDecimal`)
- **Règles métier** : taux de TVA autorisés (0 %, 5,5 %, 10 %, 20 %), validation des champs (Bean Validation)
- **Recherche** des factures par client, par période, ou les deux
- **Export JSON** d'une facture
- **Documentation interactive** via Swagger UI

## Stack technique

| Couche | Technologies |
|---|---|
| Langage | Java 17 |
| Framework | Spring Boot 3.5 (Web, Data JPA, Validation, Security) |
| Base de données | H2 en mémoire |
| Documentation | springdoc-openapi (Swagger UI) |
| Outils | Maven, Lombok |

## Architecture

```
controller/   → contrôleurs REST (ClientController, FactureController)
services/     → logique métier (interfaces + implémentations)
repository/   → accès aux données (Spring Data JPA)
enteties/     → entités JPA : Client, Facture, LigneFacture
dtos/         → objets de transfert exposés par l'API
config/       → configuration Swagger et Spring Security
```

Modèle : un `Client` possède plusieurs `Facture`, et une `Facture` contient plusieurs `LigneFacture`.

## Lancer le projet

Prérequis : Java 17 ou plus.

```bash
git clone https://github.com/erryhany/facturation.git
cd facturation
./mvnw spring-boot:run        # Windows : mvnw.cmd spring-boot:run
```

| Ressource | URL |
|---|---|
| API | http://localhost:8080/api/facturation |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Console H2 | http://localhost:8080/h2-console (JDBC URL : `jdbc:h2:mem:testdb`, utilisateur `sa`) |

## Endpoints principaux

### Clients — `/api/facturation/clients`

| Méthode | Route | Description |
|---|---|---|
| GET | `/` | Liste des clients |
| GET | `/{id}` | Détail d'un client |
| POST | `/` | Créer un client |
| PUT | `/{id}` | Modifier un client |
| DELETE | `/{id}` | Supprimer un client |
| GET | `/search?nom=` | Rechercher par nom |
| GET | `/check-email?email=` | Vérifier si un email existe |
| GET | `/check-siret?siret=` | Vérifier si un SIRET existe |

### Factures — `/api/facturation/factures`

| Méthode | Route | Description |
|---|---|---|
| GET | `/` | Liste des factures |
| GET | `/{id}` | Détail d'une facture |
| POST | `/` | Créer une facture avec ses lignes |
| PUT | `/{id}` | Modifier une facture |
| DELETE | `/{id}` | Supprimer une facture |
| GET | `/client/{clientId}` | Factures d'un client |
| GET | `/search/period` | Factures sur une période |
| GET | `/search/client-period` | Factures d'un client sur une période |
| GET | `/{id}/export` | Export JSON d'une facture |
| POST | `/validate` | Valider une facture sans l'enregistrer |

## Pistes d'évolution

- Authentification JWT et rôles ADMIN / USER sur les endpoints
- Base PostgreSQL avec Docker Compose
- Export PDF des factures
- Tests unitaires et d'intégration (JUnit 5, MockMvc)

## Auteur

**Mourad Erryhany** — Développeur Java / Spring Boot · [GitHub](https://github.com/erryhany)
