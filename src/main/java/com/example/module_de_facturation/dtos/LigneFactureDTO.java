package com.example.module_de_facturation.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * DTO pour la transfert des données de ligne de facture
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFactureDTO {
    private Long id;
    private String description;
    private Integer quantite;
    private BigDecimal prixUnitaireHt;
    private BigDecimal tauxTva;
    private BigDecimal totalHt;
    private BigDecimal totalTva;
    private BigDecimal totalTtc;
}

