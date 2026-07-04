package com.autocredit.autocreditbackend.modules.creditos.dto;

import com.autocredit.autocreditbackend.modules.creditos.enums.Capitalizacion;
import com.autocredit.autocreditbackend.modules.creditos.enums.TipoGracia;
import com.autocredit.autocreditbackend.modules.creditos.enums.TipoTasa;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreditoFormDTO {

    @NotBlank(message = "El cliente es obligatorio")
    private String clienteId;

    @NotBlank(message = "El vehículo es obligatorio")
    private String vehiculoId;

    @NotNull(message = "La moneda es obligatoria")
    private MonedaVehiculo moneda;

    @NotNull @Positive(message = "El precio del vehículo debe ser mayor a 0")
    private Double precioVehiculo;

    @NotNull @PositiveOrZero(message = "La cuota inicial no puede ser negativa")
    private Double cuotaInicial;

    @NotNull @Min(value = 1, message = "El plazo debe ser al menos 1 mes")
    private Integer plazoMeses;

    @PositiveOrZero(message = "El balloon no puede ser negativo")
    private Double cuotaBalloon;

    @NotNull(message = "El tipo de tasa es obligatorio")
    private TipoTasa tipoTasa;

    @NotNull @Positive(message = "La tasa debe ser mayor a 0%")
    private Double valorTasa;

    private Capitalizacion capitalizacion;

    @NotNull(message = "El tipo de gracia es obligatorio")
    private TipoGracia tipoGracia;

    @PositiveOrZero
    private Integer mesesGracia;

    private Double seguroVehicularAnual;
    private Double seguroDesgravamen;
    private Double comisionGastos;

    @NotNull(message = "La tasa de descuento para VAN es obligatoria")
    private Double tasaDescuentoVan;
}