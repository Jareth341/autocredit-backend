package com.autocredit.autocreditbackend.modules.creditos.entity;

import com.autocredit.autocreditbackend.modules.creditos.enums.Capitalizacion;
import com.autocredit.autocreditbackend.modules.creditos.enums.TipoGracia;
import com.autocredit.autocreditbackend.modules.creditos.enums.TipoTasa;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "creditos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private String vehiculoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MonedaVehiculo moneda;

    @Column(nullable = false)
    private Double precioVehiculo;

    @Column(nullable = false)
    private Double cuotaInicial;

    @Column(nullable = false)
    private Integer plazoMeses;

    private Double cuotaBalloon;

    @Column(nullable = false)
    private Double montoAFinanciar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTasa tipoTasa;

    @Column(nullable = false)
    private Double valorTasa;

    @Enumerated(EnumType.STRING)
    private Capitalizacion capitalizacion;

    private Double tasaEfectivaMensual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoGracia tipoGracia;

    private Integer mesesGracia;

    private Double seguroVehicularAnual;
    private Double seguroDesgravamen;
    private Double comisionGastos;

    @Column(nullable = false)
    private Double tasaDescuentoVan;

    private LocalDate fechaRegistro;
}