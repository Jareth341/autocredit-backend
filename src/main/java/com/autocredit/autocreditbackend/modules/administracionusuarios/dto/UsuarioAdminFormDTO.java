package com.autocredit.autocreditbackend.modules.administracionusuarios.dto;

import com.autocredit.autocreditbackend.modules.autenticacion.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioAdminFormDTO {

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    @NotBlank(message = "La entidad es obligatoria")
    private String entidad;

    private String passwordTemporal;
}