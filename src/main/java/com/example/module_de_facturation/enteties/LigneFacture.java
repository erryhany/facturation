package com.example.module_de_facturation.enteties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "lignes_facture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La description de la ligne est obligatoire")
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @NotNull(message = "Le prix unitaire HT est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix unitaire HT doit être supérieur à 0")
    @Column(name = "prix_unitaire_ht", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaireHt;

    @NotNull(message = "Le taux de TVA est obligatoire")
    @Column(name = "taux_tva", nullable = false, precision = 5, scale = 2)
    private BigDecimal tauxTva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;

    public BigDecimal getTotalHt() {
        return prixUnitaireHt.multiply(BigDecimal.valueOf(quantite));
    }

    public BigDecimal getTotalTva() {
        return getTotalHt().multiply(tauxTva).divide(BigDecimal.valueOf(100));
    }

    public BigDecimal getTotalTtc() {
        return getTotalHt().add(getTotalTva());
    }
}

