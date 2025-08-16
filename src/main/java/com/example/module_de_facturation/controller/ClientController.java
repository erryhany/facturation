package com.example.module_de_facturation.controller;


import com.example.module_de_facturation.dtos.ClientDTO;
import com.example.module_de_facturation.services.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des clients
 * Expose les endpoints CRUD pour les clients
 */
@RestController
@RequestMapping("/api/facturation/clients")
@CrossOrigin(origins = "*")
@Tag(name = "Gestion des Clients", description = "API pour la gestion des clients")
public class ClientController {

    @Autowired
    private IClientService clientService;

    /**
     * Récupère tous les clients
     * @return liste de tous les clients
     */
    @GetMapping
    @Operation(summary = "Récupérer tous les clients", description = "Retourne la liste de tous les clients")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        try {
            List<ClientDTO> clients = clientService.getAllClients();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère un client par son ID
     * @param id l'ID du client
     * @return le client correspondant
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client par ID", description = "Retourne un client spécifique par son ID")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        try {
            return clientService.getClientById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crée un nouveau client
     * @param clientDTO les données du client à créer
     * @return le client créé
     */
    @PostMapping
    @Operation(summary = "Créer un nouveau client", description = "Crée un nouveau client avec les données fournies")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        try {
            ClientDTO createdClient = clientService.createClient(clientDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour un client existant
     * @param id l'ID du client à mettre à jour
     * @param clientDTO les nouvelles données du client
     * @return le client mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un client", description = "Met à jour un client existant avec les nouvelles données")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        try {
            ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Supprime un client
     * @param id l'ID du client à supprimer
     * @return réponse de confirmation
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprime un client par son ID")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Recherche des clients par nom
     * @param nom le nom ou partie du nom à rechercher
     * @return liste des clients correspondants
     */
    @GetMapping("/search")
    @Operation(summary = "Rechercher des clients par nom", description = "Recherche des clients par nom ou partie du nom")
    public ResponseEntity<List<ClientDTO>> searchClientsByName(@RequestParam String nom) {
        try {
            List<ClientDTO> clients = clientService.searchClientsByName(nom);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Vérifie si un email est déjà utilisé
     * @param email l'email à vérifier
     * @return true si l'email est déjà utilisé, false sinon
     */
    @GetMapping("/check-email")
    @Operation(summary = "Vérifier la disponibilité d'un email", description = "Vérifie si un email est déjà utilisé")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        try {
            boolean isUsed = clientService.isEmailAlreadyUsed(email);
            return ResponseEntity.ok(isUsed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Vérifie si un SIRET est déjà utilisé
     * @param siret le SIRET à vérifier
     * @return true si le SIRET est déjà utilisé, false sinon
     */
    @GetMapping("/check-siret")
    @Operation(summary = "Vérifier la disponibilité d'un SIRET", description = "Vérifie si un SIRET est déjà utilisé")
    public ResponseEntity<Boolean> checkSiretAvailability(@RequestParam String siret) {
        try {
            boolean isUsed = clientService.isSiretAlreadyUsed(siret);
            return ResponseEntity.ok(isUsed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

