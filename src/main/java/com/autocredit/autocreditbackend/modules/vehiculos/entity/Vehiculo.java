package com.autocredit.autocreditbackend.modules.vehiculos.entity;

import com.autocredit.autocreditbackend.modules.vehiculos.enums.CondicionVehiculo;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.TipoVehiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vehiculos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer anio;

    private String version;

    private String color;

    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CondicionVehiculo condicion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MonedaVehiculo moneda;

    @Column(nullable = false)
    private Double precioVenta;

    @Column(nullable = false)
    private Double cuotaInicial;

    private String concesionario;
    private String vendedor;
    private String telefonoConcesionario;

    @Column(length = 1000)
    private String observaciones;

    private LocalDate fechaRegistro;
}