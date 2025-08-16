package com.example.module_de_facturation.services;


import com.example.module_de_facturation.dtos.ClientDTO;
import com.example.module_de_facturation.enteties.Client;

import java.util.List;
import java.util.Optional;

/**
 * Interface service pour la gestion des clients
 * Définit les opérations métier sur les clients
 */
public interface IClientService {

    /**
     * Récupère tous les clients
     * @return liste de tous les clients
     */
    List<ClientDTO> getAllClients();

    /**
     * Récupère un client par son ID
     * @param id l'ID du client
     * @return Optional contenant le client s'il existe
     */
    Optional<ClientDTO> getClientById(Long id);

    /**
     * Crée un nouveau client
     * @param clientDTO les données du client à créer
     * @return le client créé
     * @throws IllegalArgumentException si les données sont invalides
     */
    ClientDTO createClient(ClientDTO clientDTO);

    /**
     * Met à jour un client existant
     * @param id l'ID du client à mettre à jour
     * @param clientDTO les nouvelles données du client
     * @return le client mis à jour
     * @throws IllegalArgumentException si le client n'existe pas ou si les données sont invalides
     */
    ClientDTO updateClient(Long id, ClientDTO clientDTO);

    /**
     * Supprime un client
     * @param id l'ID du client à supprimer
     * @throws IllegalArgumentException si le client n'existe pas
     */
    void deleteClient(Long id);

    /**
     * Recherche des clients par nom
     * @param nom le nom ou partie du nom à rechercher
     * @return liste des clients correspondants
     */
    List<ClientDTO> searchClientsByName(String nom);

    /**
     * Vérifie si un email est déjà utilisé
     * @param email l'email à vérifier
     * @return true si l'email est déjà utilisé, false sinon
     */
    boolean isEmailAlreadyUsed(String email);

    /**
     * Vérifie si un SIRET est déjà utilisé
     * @param siret le SIRET à vérifier
     * @return true si le SIRET est déjà utilisé, false sinon
     */
    boolean isSiretAlreadyUsed(String siret);
}
