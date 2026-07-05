package com.autocredit.autocreditbackend.modules.administracionusuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticasUsuariosDTO {
    private long totalUsuarios;
    private long activos;
    private long inactivos;
    private long administradores;
}