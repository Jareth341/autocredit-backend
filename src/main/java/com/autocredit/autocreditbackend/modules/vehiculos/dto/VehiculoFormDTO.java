package com.autocredit.autocreditbackend.modules.vehiculos.dto;

import com.autocredit.autocreditbackend.modules.vehiculos.enums.CondicionVehiculo;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.TipoVehiculo;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VehiculoFormDTO {

    @NotBlank(message = "El cliente es obligatorio")
    private String clienteId;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 2000, message = "El año debe ser mayor o igual a 2000")
    private Integer anio;

    private String version;
    private String color;
    private TipoVehiculo tipoVehiculo;

    @NotNull(message = "La condición es obligatoria")
    private CondicionVehiculo condicion;

    @NotNull(message = "La moneda es obligatoria")
    private MonedaVehiculo moneda;

    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private Double precioVenta;

    @NotNull(message = "La cuota inicial es obligatoria")
    @PositiveOrZero(message = "La cuota inicial no puede ser negativa")
    private Double cuotaInicial;

    private String concesionario;
    private String vendedor;
    private String telefonoConcesionario;
    private String observaciones;
}