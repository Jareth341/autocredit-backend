package com.autocredit.autocreditbackend.modules.simulacion.dto;

import com.autocredit.autocreditbackend.modules.creditos.enums.TipoGracia;
import lombok.Data;

@Data
public class ParametrosCronogramaDTO {
    private double montoAFinanciar;
    private double temPorcentual;
    private int plazoMeses;
    private double cuotaBalloon;
    private TipoGracia tipoGracia;
    private int mesesGracia;
    private double seguroVehicularAnual;
    private double precioVehiculo;
    private double seguroDesgravamenPct;
    private double comisionGastos;
}