package com.example.module_de_facturation.repository;


import com.example.module_de_facturation.enteties.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des clients
 * Fournit les méthodes CRUD de base et des requêtes personnalisées
 */
@Repository
public interface IClientRepository extends JpaRepository<Client, Long> {

    /**
     * Trouve un client par son email
     * @param email l'email du client
     * @return Optional contenant le client s'il existe
     */
    Optional<Client> findByEmail(String email);

    /**
     * Trouve un client par son SIRET
     * @param siret le SIRET du client
     * @return Optional contenant le client s'il existe
     */
    Optional<Client> findBySiret(String siret);

    /**
     * Trouve des clients par nom (recherche partielle insensible à la casse)
     * @param nom le nom ou partie du nom à rechercher
     * @return liste des clients correspondants
     */
    @Query("SELECT c FROM Client c WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Client> findByNomContainingIgnoreCase(@Param("nom") String nom);

    /**
     * Vérifie si un email existe déjà
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un SIRET existe déjà
     * @param siret le SIRET à vérifier
     * @return true si le SIRET existe, false sinon
     */
    boolean existsBySiret(String siret);
}
