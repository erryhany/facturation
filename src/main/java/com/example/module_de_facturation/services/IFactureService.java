package com.example.module_de_facturation.services;


import com.example.module_de_facturation.dtos.FactureDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface service pour la gestion des factures
 * Définit les opérations métier sur les factures
 */
public interface IFactureService {

    /**
     * Récupère toutes les factures
     * @return liste de toutes les factures
     */
    List<FactureDTO> getAllFactures();

    /**
     * Récupère une facture par son ID
     * @param id l'ID de la facture
     * @return Optional contenant la facture si elle existe
     */
    Optional<FactureDTO> getFactureById(Long id);

    /**
     * Crée une nouvelle facture
     * @param factureDTO les données de la facture à créer
     * @return la facture créée
     * @throws IllegalArgumentException si les données sont invalides
     */
    FactureDTO createFacture(FactureDTO factureDTO);

    /**
     * Met à jour une facture existante
     * @param id l'ID de la facture à mettre à jour
     * @param factureDTO les nouvelles données de la facture
     * @return la facture mise à jour
     * @throws IllegalArgumentException si la facture n'existe pas ou si les données sont invalides
     */
    FactureDTO updateFacture(Long id, FactureDTO factureDTO);

    /**
     * Supprime une facture
     * @param id l'ID de la facture à supprimer
     * @throws IllegalArgumentException si la facture n'existe pas
     */
    void deleteFacture(Long id);

    /**
     * Récupère toutes les factures d'un client
     * @param clientId l'ID du client
     * @return liste des factures du client
     */
    List<FactureDTO> getFacturesByClient(Long clientId);

    /**
     * Recherche des factures par période
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return liste des factures dans la période
     */
    List<FactureDTO> getFacturesByPeriod(LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Recherche des factures par client et période
     * @param clientId l'ID du client
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return liste des factures du client dans la période
     */
    List<FactureDTO> getFacturesByClientAndPeriod(Long clientId, LocalDateTime dateDebut, LocalDateTime dateFin);

    /**
     * Exporte une facture au format JSON
     * @param id l'ID de la facture à exporter
     * @return la facture au format JSON
     * @throws IllegalArgumentException si la facture n'existe pas
     */
    String exportFactureToJson(Long id);

    /**
     * Valide les règles métier d'une facture
     * @param factureDTO la facture à valider
     * @throws IllegalArgumentException si la facture n'est pas valide
     */
    void validateFacture(FactureDTO factureDTO);
}

