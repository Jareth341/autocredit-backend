package com.autocredit.autocreditbackend.modules.autenticacion.repository;

import com.autocredit.autocreditbackend.modules.autenticacion.entity.Usuario;
import com.autocredit.autocreditbackend.modules.autenticacion.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrCorreoContainingIgnoreCase(
            String nombres, String apellidos, String correo
    );
}