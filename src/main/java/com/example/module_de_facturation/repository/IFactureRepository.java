package com.example.module_de_facturation.repository;


import com.example.module_de_facturation.enteties.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour la gestion des factures
 * Fournit les méthodes CRUD de base et des requêtes personnalisées
 */
@Repository
public interface IFactureRepository extends JpaRepository<Facture, Long> {

    /**
     * Trouve toutes les factures d'un client
     * @param clientId l'ID du client
     * @return liste des factures du client
     */
    List<Facture> findByClientId(Long clientId);

    /**
     * Trouve des factures par date de facturation
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return liste des factures dans la période
     */
    @Query("SELECT f FROM Facture f WHERE f.dateFacture BETWEEN :dateDebut AND :dateFin ORDER BY f.dateFacture DESC")
    List<Facture> findByDateFactureBetween(@Param("dateDebut") LocalDateTime dateDebut,
                                           @Param("dateFin") LocalDateTime dateFin);

    /**
     * Trouve des factures par client et période
     * @param clientId l'ID du client
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return liste des factures du client dans la période
     */
    @Query("SELECT f FROM Facture f WHERE f.client.id = :clientId AND f.dateFacture BETWEEN :dateDebut AND :dateFin ORDER BY f.dateFacture DESC")
    List<Facture> findByClientIdAndDateFactureBetween(@Param("clientId") Long clientId,
                                                      @Param("dateDebut") LocalDateTime dateDebut,
                                                      @Param("dateFin") LocalDateTime dateFin);

    /**
     * Trouve une facture par son numéro
     * @param numero le numéro de la facture
     * @return la facture correspondante
     */
    Facture findByNumero(String numero);

    /**
     * Vérifie si un numéro de facture existe déjà
     * @param numero le numéro à vérifier
     * @return true si le numéro existe, false sinon
     */
    boolean existsByNumero(String numero);
}

