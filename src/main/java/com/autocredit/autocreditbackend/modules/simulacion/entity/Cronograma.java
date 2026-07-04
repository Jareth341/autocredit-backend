package com.autocredit.autocreditbackend.modules.simulacion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cronogramas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cronograma {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String creditoId;

    @Column(nullable = false)
    private Double montoFinanciado;

    @Column(nullable = false)
    private Double cuotaMensual;

    @Column(nullable = false)
    private Integer plazoMeses;

    @Column(nullable = false)
    private Double tem;

    private Double interesTotal;
    private Double seguroTotal;
    private Double comisionTotal;
    private Double costoTotalCredito;

    @ElementCollection
    @CollectionTable(name = "periodos_pago", joinColumns = @JoinColumn(name = "cronograma_id"))
    @OrderColumn(name = "orden")
    private List<PeriodoPago> periodos;

    @Column(nullable = false)
    private boolean guardado;

    private LocalDateTime fechaGeneracion;
}