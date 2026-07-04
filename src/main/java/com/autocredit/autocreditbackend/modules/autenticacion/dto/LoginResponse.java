package com.autocredit.autocreditbackend.modules.autenticacion.dto;

import com.autocredit.autocreditbackend.modules.autenticacion.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Usuario usuario;
}