package com.autocredit.autocreditbackend.modules.simulacion.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoPago {
    private Integer numero;
    private String estado; // GRACIA | NORMAL | ULTIMA_CUOTA
    private Double saldoInicial;
    private Double interes;
    private Double amortizacion;
    private Double cuotaBase;
    private Double seguro;
    private Double comision;
    private Double balloon;
    private Double cuotaTotal;
    private Double saldoFinal;
}