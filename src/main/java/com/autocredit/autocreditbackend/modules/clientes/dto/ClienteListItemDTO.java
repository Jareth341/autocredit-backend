package com.autocredit.autocreditbackend.modules.clientes.dto;

import com.autocredit.autocreditbackend.modules.clientes.enums.EstadoCliente;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ClienteListItemDTO {
    private String id;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String correo;
    private String telefono;
    private String vehiculoAsociado;
    private Double ingresoMensual;
    private EstadoCliente estado;
    private LocalDate fechaRegistro;
}