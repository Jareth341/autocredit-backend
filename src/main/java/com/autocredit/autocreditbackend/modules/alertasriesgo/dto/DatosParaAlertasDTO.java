package com.autocredit.autocreditbackend.modules.alertasriesgo.dto;

import com.autocredit.autocreditbackend.modules.creditos.enums.TipoGracia;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import lombok.Data;

@Data
public class DatosParaAlertasDTO {
    private double cuotaMensual;
    private double ingresoMensualCliente;
    private double cuotaBalloon;
    private double montoFinanciado;
    private MonedaVehiculo monedaCredito;
    private TipoGracia tipoGracia;
    private int mesesGracia;
}