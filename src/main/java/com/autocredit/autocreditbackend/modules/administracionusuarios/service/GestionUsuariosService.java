package com.autocredit.autocreditbackend.modules.administracionusuarios.service;

import com.autocredit.autocreditbackend.core.exception.DuplicateResourceException;
import com.autocredit.autocreditbackend.core.exception.ResourceNotFoundException;
import com.autocredit.autocreditbackend.modules.administracionusuarios.dto.EstadisticasUsuariosDTO;
import com.autocredit.autocreditbackend.modules.administracionusuarios.dto.UsuarioAdminFormDTO;
import com.autocredit.autocreditbackend.modules.autenticacion.entity.Usuario;
import com.autocredit.autocreditbackend.modules.autenticacion.enums.Rol;
import com.autocredit.autocreditbackend.modules.autenticacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GestionUsuariosService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listar(String filtro, String rol) {
        if (filtro != null && !filtro.isBlank()) {
            return usuarioRepository
                    .findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                            filtro, filtro, filtro
                    );
        }
        if (rol != null && !rol.isBlank()) {
            return usuarioRepository.findByRol(Rol.valueOf(rol));
        }
        return usuarioRepository.findAll();
    }

    public EstadisticasUsuariosDTO obtenerEstadisticas() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        long total = usuarios.size();
        long activos = usuarios.stream().filter(Usuario::isActivo).count();
        long inactivos = total - activos;
        long administradores = usuarios.stream().filter(u -> u.getRol() == Rol.ADMINISTRADOR).count();

        return new EstadisticasUsuariosDTO(total, activos, inactivos, administradores);
    }

    public Usuario crear(UsuarioAdminFormDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new DuplicateResourceException("CORREO_DUPLICADO");
        }

        String passwordFinal = dto.getPasswordTemporal() != null && !dto.getPasswordTemporal().isBlank()
                ? dto.getPasswordTemporal()
                : "Temporal123";

        Usuario usuario = Usuario.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .correo(dto.getCorreo())
                .usuario(dto.getUsuario())
                .password(passwordEncoder.encode(passwordFinal))
                .rol(dto.getRol())
                .entidad(dto.getEntidad())
                .activo(true)
                .fechaRegistro(LocalDate.now())
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(String id, UsuarioAdminFormDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setCorreo(dto.getCorreo());
        usuario.setUsuario(dto.getUsuario());
        usuario.setRol(dto.getRol());
        usuario.setEntidad(dto.getEntidad());

        return usuarioRepository.save(usuario);
    }

    public Usuario cambiarEstado(String id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }

    public void eliminar(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    public boolean existeCorreoDuplicado(String correo, String idExcluir) {
        return usuarioRepository.findByCorreo(correo)
                .map(u -> !u.getId().equals(idExcluir))
                .orElse(false);
    }
}