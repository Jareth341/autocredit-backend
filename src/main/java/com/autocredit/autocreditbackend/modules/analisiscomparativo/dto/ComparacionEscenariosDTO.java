package com.autocredit.autocreditbackend.modules.analisiscomparativo.dto;

import com.autocredit.autocreditbackend.modules.analisiscomparativo.entity.SimulacionGuardada;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComparacionEscenariosDTO {
    private SimulacionGuardada escenarioA;
    private SimulacionGuardada escenarioB;
    private String mejorOpcionId;
}