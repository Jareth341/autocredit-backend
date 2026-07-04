package com.autocredit.autocreditbackend.modules.autenticacion.service;

import com.autocredit.autocreditbackend.core.exception.DuplicateResourceException;
import com.autocredit.autocreditbackend.modules.autenticacion.dto.LoginRequest;
import com.autocredit.autocreditbackend.modules.autenticacion.dto.LoginResponse;
import com.autocredit.autocreditbackend.modules.autenticacion.dto.RegisterRequest;
import com.autocredit.autocreditbackend.modules.autenticacion.entity.Usuario;
import com.autocredit.autocreditbackend.modules.autenticacion.repository.UsuarioRepository;
import com.autocredit.autocreditbackend.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Usuario o contraseña inválidos");
        }

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BadCredentialsException("Usuario o contraseña inválidos"));

        String token = jwtService.generarToken(usuario);
        return new LoginResponse(token, usuario);
    }

    public Usuario register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new DuplicateResourceException("CORREO_DUPLICADO");
        }
        if (!request.getPassword().equals(request.getConfirmarPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        Usuario usuario = Usuario.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .correo(request.getCorreo())
                .usuario(request.getUsuario())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .entidad(request.getEntidad())
                .activo(true)
                .fechaRegistro(LocalDate.now())
                .build();

        return usuarioRepository.save(usuario);
    }
}