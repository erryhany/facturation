package com.example.module_de_facturation.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour la transfert des données de facture
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactureDTO {
    private Long id;
    private String numero;
    private LocalDateTime dateFacture;
    private ClientDTO client;
    private List<LigneFactureDTO> lignes;
    private BigDecimal totalHt;
    private BigDecimal totalTva;
    private BigDecimal totalTtc;
}

