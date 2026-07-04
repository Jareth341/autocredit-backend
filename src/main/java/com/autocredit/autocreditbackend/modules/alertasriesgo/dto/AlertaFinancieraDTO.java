package com.autocredit.autocreditbackend.modules.alertasriesgo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertaFinancieraDTO {
    private String tipo; // OK | WARN
    private String titulo;
    private String descripcion;
}