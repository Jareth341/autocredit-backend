package com.autocredit.autocreditbackend.modules.vehiculos.dto;

import com.autocredit.autocreditbackend.modules.vehiculos.enums.CondicionVehiculo;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VehiculoListItemDTO {
    private String id;
    private String clienteId;
    private String clienteNombre;
    private String marca;
    private String modelo;
    private Integer anio;
    private CondicionVehiculo condicion;
    private MonedaVehiculo moneda;
    private Double precioVenta;
    private String concesionario;
    private LocalDate fechaRegistro;
}