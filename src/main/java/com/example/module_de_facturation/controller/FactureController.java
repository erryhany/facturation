package com.example.module_de_facturation.controller;


import com.example.module_de_facturation.dtos.FactureDTO;
import com.example.module_de_facturation.services.IFactureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des factures
 * Expose les endpoints CRUD pour les factures et les fonctionnalités de recherche
 */
@RestController
@RequestMapping("/api/facturation/factures")
@CrossOrigin(origins = "*")
@Tag(name = "Gestion des Factures", description = "API pour la gestion des factures")
public class FactureController {

    @Autowired
    private IFactureService factureService;

    /**
     * Récupère toutes les factures
     * @return liste de toutes les factures
     */
    @GetMapping
    @Operation(summary = "Récupérer toutes les factures", description = "Retourne la liste de toutes les factures")
    public ResponseEntity<List<FactureDTO>> getAllFactures() {
        try {
            List<FactureDTO> factures = factureService.getAllFactures();
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère une facture par son ID
     * @param id l'ID de la facture
     * @return la facture correspondante
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une facture par ID", description = "Retourne une facture spécifique par son ID")
    public ResponseEntity<FactureDTO> getFactureById(@PathVariable Long id) {
        try {
            return factureService.getFactureById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crée une nouvelle facture
     * @param factureDTO les données de la facture à créer
     * @return la facture créée
     */
    @PostMapping
    @Operation(summary = "Créer une nouvelle facture", description = "Crée une nouvelle facture avec les données fournies")
    public ResponseEntity<FactureDTO> createFacture(@RequestBody FactureDTO factureDTO) {
        try {
            FactureDTO createdFacture = factureService.createFacture(factureDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFacture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour une facture existante
     * @param id l'ID de la facture à mettre à jour
     * @param factureDTO les nouvelles données de la facture
     * @return la facture mise à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une facture", description = "Met à jour une facture existante avec les nouvelles données")
    public ResponseEntity<FactureDTO> updateFacture(@PathVariable Long id, @RequestBody FactureDTO factureDTO) {
        try {
            FactureDTO updatedFacture = factureService.updateFacture(id, factureDTO);
            return ResponseEntity.ok(updatedFacture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Supprime une facture
     * @param id l'ID de la facture à supprimer
     * @return réponse de confirmation
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une facture", description = "Supprime une facture par son ID")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        try {
            factureService.deleteFacture(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère toutes les factures d'un client
     * @param clientId l'ID du client
     * @return liste des factures du client
     */
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Récupérer les factures d'un client", description = "Retourne toutes les factures d'un client spécifique")
    public ResponseEntity<List<FactureDTO>> getFacturesByClient(@PathVariable Long clientId) {
        try {
            List<FactureDTO> factures = factureService.getFacturesByClient(clientId);
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Recherche des factures par période
     * @param dateDebut date de début de la période (format: yyyy-MM-dd'T'HH:mm:ss)
     * @param dateFin date de fin de la période (format: yyyy-MM-dd'T'HH:mm:ss)
     * @return liste des factures dans la période
     */
    @GetMapping("/search/period")
    @Operation(summary = "Rechercher des factures par période", description = "Recherche des factures dans une période donnée")
    public ResponseEntity<List<FactureDTO>> getFacturesByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        try {
            List<FactureDTO> factures = factureService.getFacturesByPeriod(dateDebut, dateFin);
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Recherche des factures par client et période
     * @param clientId l'ID du client
     * @param dateDebut date de début de la période (format: yyyy-MM-dd'T'HH:mm:ss)
     * @param dateFin date de fin de la période (format: yyyy-MM-dd'T'HH:mm:ss)
     * @return liste des factures du client dans la période
     */
    @GetMapping("/search/client-period")
    @Operation(summary = "Rechercher des factures par client et période", description = "Recherche des factures d'un client dans une période donnée")
    public ResponseEntity<List<FactureDTO>> getFacturesByClientAndPeriod(
            @RequestParam Long clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        try {
            List<FactureDTO> factures = factureService.getFacturesByClientAndPeriod(clientId, dateDebut, dateFin);
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exporte une facture au format JSON
     * @param id l'ID de la facture à exporter
     * @return la facture au format JSON
     */
    @GetMapping("/{id}/export")
    @Operation(summary = "Exporter une facture en JSON", description = "Exporte une facture au format JSON")
    public ResponseEntity<String> exportFactureToJson(@PathVariable Long id) {
        try {
            String jsonFacture = factureService.exportFactureToJson(id);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .header("Content-Disposition", "attachment; filename=\"facture_" + id + ".json\"")
                    .body(jsonFacture);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Valide une facture selon les règles métier
     * @param factureDTO la facture à valider
     * @return réponse de validation
     */
    @PostMapping("/validate")
    @Operation(summary = "Valider une facture", description = "Valide une facture selon les règles métier")
    public ResponseEntity<Void> validateFacture(@RequestBody FactureDTO factureDTO) {
        try {
            factureService.validateFacture(factureDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

