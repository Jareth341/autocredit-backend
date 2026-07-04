package com.autocredit.autocreditbackend.modules.autenticacion.controller;

import com.autocredit.autocreditbackend.modules.autenticacion.dto.LoginRequest;
import com.autocredit.autocreditbackend.modules.autenticacion.dto.LoginResponse;
import com.autocredit.autocreditbackend.modules.autenticacion.dto.RegisterRequest;
import com.autocredit.autocreditbackend.modules.autenticacion.entity.Usuario;
import com.autocredit.autocreditbackend.modules.autenticacion.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
}