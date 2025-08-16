package com.example.module_de_facturation.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.module_de_facturation.dtos.FactureDTO;
import com.example.module_de_facturation.dtos.ClientDTO;
import com.example.module_de_facturation.dtos.LigneFactureDTO;
import com.example.module_de_facturation.enteties.Client;
import com.example.module_de_facturation.enteties.Facture;
import com.example.module_de_facturation.enteties.LigneFacture;
import com.example.module_de_facturation.repository.IClientRepository;
import com.example.module_de_facturation.repository.IFactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service pour la gestion des factures
 * Gère la logique métier, la validation et les calculs automatiques
 */
@Service
@Transactional
public class FactureServiceImpl implements IFactureService {

    @Autowired
    private IFactureRepository factureRepository;

    @Autowired
    private IClientRepository clientRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Taux de TVA autorisés
    private static final BigDecimal[] TAUX_TVA_AUTORISES = {
            BigDecimal.ZERO, new BigDecimal("5.5"), new BigDecimal("10"), new BigDecimal("20")
    };

    @Override
    public List<FactureDTO> getAllFactures() {
        return factureRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FactureDTO> getFactureById(Long id) {
        return factureRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public FactureDTO createFacture(FactureDTO factureDTO) {
        // Validation des données
        validateFacture(factureDTO);

        // Récupération du client
        Client client = clientRepository.findById(factureDTO.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + factureDTO.getClient().getId()));

        // Création de la facture
        Facture facture = new Facture();
        facture.setClient(client);
        facture.setDateFacture(factureDTO.getDateFacture());

        // Ajout des lignes
        if (factureDTO.getLignes() != null) {
            for (LigneFactureDTO ligneDTO : factureDTO.getLignes()) {
                LigneFacture ligne = new LigneFacture();
                ligne.setDescription(ligneDTO.getDescription());
                ligne.setQuantite(ligneDTO.getQuantite());
                ligne.setPrixUnitaireHt(ligneDTO.getPrixUnitaireHt());
                ligne.setTauxTva(ligneDTO.getTauxTva());
                facture.ajouterLigne(ligne);
            }
        }

        // Calcul automatique des totaux
        facture.calculerTotaux();

        // Sauvegarde
        Facture savedFacture = factureRepository.save(facture);
        return convertToDTO(savedFacture);
    }

    @Override
    public FactureDTO updateFacture(Long id, FactureDTO factureDTO) {
        // Vérification de l'existence de la facture
        Facture existingFacture = factureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facture non trouvée avec l'ID: " + id));

        // Validation des données
        validateFacture(factureDTO);

        // Récupération du client
        Client client = clientRepository.findById(factureDTO.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + factureDTO.getClient().getId()));

        // Mise à jour de la facture
        existingFacture.setClient(client);
        existingFacture.setDateFacture(factureDTO.getDateFacture());

        // Suppression des anciennes lignes
        existingFacture.getLignes().clear();

        // Ajout des nouvelles lignes
        if (factureDTO.getLignes() != null) {
            for (LigneFactureDTO ligneDTO : factureDTO.getLignes()) {
                LigneFacture ligne = new LigneFacture();
                ligne.setDescription(ligneDTO.getDescription());
                ligne.setQuantite(ligneDTO.getQuantite());
                ligne.setPrixUnitaireHt(ligneDTO.getPrixUnitaireHt());
                ligne.setTauxTva(ligneDTO.getTauxTva());
                existingFacture.ajouterLigne(ligne);
            }
        }

        // Calcul automatique des totaux
        existingFacture.calculerTotaux();

        // Sauvegarde
        Facture updatedFacture = factureRepository.save(existingFacture);
        return convertToDTO(updatedFacture);
    }

    @Override
    public void deleteFacture(Long id) {
        if (!factureRepository.existsById(id)) {
            throw new IllegalArgumentException("Facture non trouvée avec l'ID: " + id);
        }
        factureRepository.deleteById(id);
    }

    @Override
    public List<FactureDTO> getFacturesByClient(Long clientId) {
        return factureRepository.findByClientId(clientId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getFacturesByPeriod(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return factureRepository.findByDateFactureBetween(dateDebut, dateFin)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FactureDTO> getFacturesByClientAndPeriod(Long clientId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        return factureRepository.findByClientIdAndDateFactureBetween(clientId, dateDebut, dateFin)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String exportFactureToJson(Long id) {
        FactureDTO facture = getFactureById(id)
                .orElseThrow(() -> new IllegalArgumentException("Facture non trouvée avec l'ID: " + id));

        try {
            return objectMapper.writeValueAsString(facture);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la génération du JSON", e);
        }
    }

    @Override
    public void validateFacture(FactureDTO factureDTO) {
        if (factureDTO == null) {
            throw new IllegalArgumentException("Les données de la facture ne peuvent pas être nulles");
        }

        if (factureDTO.getClient().getId() == null) {
            throw new IllegalArgumentException("L'ID du client est obligatoire");
        }

        if (factureDTO.getDateFacture() == null) {
            throw new IllegalArgumentException("La date de facturation est obligatoire");
        }

        if (factureDTO.getLignes() == null || factureDTO.getLignes().isEmpty()) {
            throw new IllegalArgumentException("Une facture doit avoir au moins une ligne");
        }

        // Validation des lignes
        for (LigneFactureDTO ligne : factureDTO.getLignes()) {
            validateLigneFacture(ligne);
        }
    }

    /**
     * Valide une ligne de facture
     * @param ligne la ligne à valider
     * @throws IllegalArgumentException si la ligne n'est pas valide
     */
    private void validateLigneFacture(LigneFactureDTO ligne) {
        if (ligne.getDescription() == null || ligne.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description de la ligne est obligatoire");
        }

        if (ligne.getQuantite() == null || ligne.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité doit être strictement positive");
        }

        if (ligne.getPrixUnitaireHt() == null || ligne.getPrixUnitaireHt().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix unitaire HT doit être strictement positif");
        }

        if (ligne.getTauxTva() == null) {
            throw new IllegalArgumentException("Le taux de TVA est obligatoire");
        }

        // Vérification que le taux de TVA est autorisé
        boolean tauxAutorise = false;
        for (BigDecimal taux : TAUX_TVA_AUTORISES) {
            if (ligne.getTauxTva().compareTo(taux) == 0) {
                tauxAutorise = true;
                break;
            }
        }

        if (!tauxAutorise) {
            throw new IllegalArgumentException("Le taux de TVA doit être 0%, 5.5%, 10% ou 20%");
        }
    }

    /**
     * Convertit une entité Facture en DTO
     * @param facture l'entité à convertir
     * @return le DTO correspondant
     */
    private FactureDTO convertToDTO(Facture facture) {
        FactureDTO dto = new FactureDTO();
        dto.setId(facture.getId());
        dto.setNumero(facture.getNumero());
        dto.setDateFacture(facture.getDateFacture());
        dto.setTotalHt(facture.getTotalHt());
        dto.setTotalTva(facture.getTotalTva());
        dto.setTotalTtc(facture.getTotalTtc());

        if (facture.getClient() != null) {
            ClientDTO clientDTO = new ClientDTO();
            clientDTO.setId(facture.getClient().getId());
            clientDTO.setNom(facture.getClient().getNom());
            clientDTO.setEmail(facture.getClient().getEmail());
            clientDTO.setSiret(facture.getClient().getSiret());
            clientDTO.setDateCreation(facture.getClient().getDateCreation());
            dto.setClient(clientDTO);
        }

        if (facture.getLignes() != null) {
            List<LigneFactureDTO> lignesDTO = facture.getLignes()
                    .stream()
                    .map(this::convertLigneToDTO)
                    .collect(Collectors.toList());
            dto.setLignes(lignesDTO);
        }

        return dto;
    }

    /**
     * Convertit une entité LigneFacture en DTO
     * @param ligne l'entité à convertir
     * @return le DTO correspondant
     */
    private LigneFactureDTO convertLigneToDTO(LigneFacture ligne) {
        LigneFactureDTO dto = new LigneFactureDTO();
        dto.setId(ligne.getId());
        dto.setDescription(ligne.getDescription());
        dto.setQuantite(ligne.getQuantite());
        dto.setPrixUnitaireHt(ligne.getPrixUnitaireHt());
        dto.setTauxTva(ligne.getTauxTva());
        dto.setTotalHt(ligne.getTotalHt());
        dto.setTotalTva(ligne.getTotalTva());
        dto.setTotalTtc(ligne.getTotalTtc());
        return dto;
    }
}

