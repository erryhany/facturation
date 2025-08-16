package com.example.module_de_facturation.services;


import com.example.module_de_facturation.dtos.ClientDTO;
import com.example.module_de_facturation.enteties.Client;
import com.example.module_de_facturation.repository.IClientRepository;
import com.example.module_de_facturation.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour la gestion des clients
 * Gère la logique métier et la validation des données
 */
@Service
@Transactional
public class ClientServiceImpl implements IClientService {

    @Autowired
    private IClientRepository clientRepository;

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClientDTO> getClientById(Long id) {
        return clientRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        // Validation
        if (clientDTO == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }

        // Création de l'entité
        Client client = new Client();
        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());
        client.setSiret(clientDTO.getSiret());
        // dateCreation est automatique avec @PrePersist

        // Sauvegarde
        Client savedClient = clientRepository.save(client);

        // Conversion en DTO et retour
        return convertToDTO(savedClient);
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        // Vérification de l'existence du client
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + id));

        // Validation des données
        validateClientData(clientDTO);

        // Vérification de l'unicité de l'email et du SIRET (sauf pour le client actuel)
        if (!existingClient.getEmail().equals(clientDTO.getEmail()) && isEmailAlreadyUsed(clientDTO.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }
        if (!existingClient.getSiret().equals(clientDTO.getSiret()) && isSiretAlreadyUsed(clientDTO.getSiret())) {
            throw new IllegalArgumentException("Un client avec ce SIRET existe déjà");
        }

        // Mise à jour du client
        existingClient.setNom(clientDTO.getNom());
        existingClient.setEmail(clientDTO.getEmail());
        existingClient.setSiret(clientDTO.getSiret());

        Client updatedClient = clientRepository.save(existingClient);
        return convertToDTO(updatedClient);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client non trouvé avec l'ID: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    public List<ClientDTO> searchClientsByName(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return getAllClients();
        }

        return clientRepository.findByNomContainingIgnoreCase(nom.trim())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmailAlreadyUsed(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public boolean isSiretAlreadyUsed(String siret) {
        return clientRepository.existsBySiret(siret);
    }

    /**
     * Valide les données d'un client
     * @param clientDTO les données à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateClientData(ClientDTO clientDTO) {
        if (clientDTO == null) {
            throw new IllegalArgumentException("Les données du client ne peuvent pas être nulles");
        }

        if (clientDTO.getNom() == null || clientDTO.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }

        if (clientDTO.getEmail() == null || clientDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email du client est obligatoire");
        }

        if (clientDTO.getSiret() == null || clientDTO.getSiret().trim().isEmpty()) {
            throw new IllegalArgumentException("Le SIRET du client est obligatoire");
        }

        // Validation du format SIRET (14 chiffres)
        if (!clientDTO.getSiret().matches("^[0-9]{14}$")) {
            throw new IllegalArgumentException("Le SIRET doit contenir exactement 14 chiffres");
        }

        // Validation basique du format email
        if (!clientDTO.getEmail().contains("@")) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
    }

    /**
     * Convertit une entité Client en DTO
     * @param client l'entité à convertir
     * @return le DTO correspondant
     */
    private ClientDTO convertToDTO(Client client) {
        if (client == null) return null;

        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setSiret(client.getSiret());
        dto.setDateCreation(client.getDateCreation());

        return dto;
    }
}
