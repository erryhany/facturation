package com.example.module_de_facturation.enteties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une facture
 */
@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le numéro de facture est obligatoire")
    @Column(name = "numero", nullable = false, unique = true, length = 50)
    private String numero;

    @NotNull(message = "La date de facture est obligatoire")
    @Column(name = "date_facture", nullable = false)
    private LocalDateTime dateFacture;

    @NotNull(message = "Le client est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Size(min = 1, message = "Une facture doit avoir au moins une ligne")
    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LigneFacture> lignes = new ArrayList<>();

    @Column(name = "total_ht", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHt = BigDecimal.ZERO;

    @Column(name = "total_tva", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalTva = BigDecimal.ZERO;

    @Column(name = "total_ttc", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalTtc = BigDecimal.ZERO;

    /**
     * Calcule automatiquement les totaux HT, TVA et TTC
     */
    public void calculerTotaux() {
        this.totalHt = BigDecimal.ZERO;
        this.totalTva = BigDecimal.ZERO;
        this.totalTtc = BigDecimal.ZERO;

        for (LigneFacture ligne : lignes) {
            this.totalHt = this.totalHt.add(ligne.getTotalHt());
            this.totalTva = this.totalTva.add(ligne.getTotalTva());
        }

        this.totalTtc = this.totalHt.add(this.totalTva);
    }

    /**
     * Ajoute une ligne à la facture
     */
    public void ajouterLigne(LigneFacture ligne) {
        lignes.add(ligne);
        ligne.setFacture(this);
        calculerTotaux();
    }

    /**
     * Supprime une ligne de la facture
     */
    public void supprimerLigne(LigneFacture ligne) {
        lignes.remove(ligne);
        ligne.setFacture(null);
        calculerTotaux();
    }

    /**
     * Vérifie si la facture a des lignes
     */
    public boolean hasLignes() {
        return lignes != null && !lignes.isEmpty();
    }
}

