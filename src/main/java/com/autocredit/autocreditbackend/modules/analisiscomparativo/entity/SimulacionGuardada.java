package com.autocredit.autocreditbackend.modules.analisiscomparativo.entity;

import com.autocredit.autocreditbackend.modules.creditos.enums.TipoTasa;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "simulaciones_guardadas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulacionGuardada {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private String creditoId;

    private String clienteNombre;
    private String entidad;

    @Enumerated(EnumType.STRING)
    private MonedaVehiculo moneda;

    private Double cuotaMensual;
    private Double tcea;
    private Double tirMensual;
    private Double costoTotalCredito;
    private Integer plazoMeses;
    private String nivelRiesgo; // BAJO | MODERADO | ALTO

    @Enumerated(EnumType.STRING)
    private TipoTasa tipoTasa;

    private Double valorTasa;
    private LocalDate fecha;

    @Builder.Default
    private String estado = "GUARDADA";
}