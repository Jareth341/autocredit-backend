package com.autocredit.autocreditbackend.modules.administracionusuarios.controller;

import com.autocredit.autocreditbackend.modules.administracionusuarios.dto.CambiarEstadoDTO;
import com.autocredit.autocreditbackend.modules.administracionusuarios.dto.EstadisticasUsuariosDTO;
import com.autocredit.autocreditbackend.modules.administracionusuarios.dto.UsuarioAdminFormDTO;
import com.autocredit.autocreditbackend.modules.administracionusuarios.service.GestionUsuariosService;
import com.autocredit.autocreditbackend.modules.autenticacion.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private final GestionUsuariosService gestionUsuariosService;

    @GetMapping
    public List<Usuario> listar(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) String rol
    ) {
        return gestionUsuariosService.listar(buscar, rol);
    }

    @GetMapping("/estadisticas")
    public EstadisticasUsuariosDTO obtenerEstadisticas() {
        return gestionUsuariosService.obtenerEstadisticas();
    }

    @GetMapping("/verificar-correo")
    public ResponseEntity<Map<String, Boolean>> verificarCorreoDuplicado(
            @RequestParam String correo,
            @RequestParam(required = false) String excluirId
    ) {
        boolean duplicado = gestionUsuariosService.existeCorreoDuplicado(correo, excluirId);
        return ResponseEntity.ok(Map.of("duplicado", duplicado));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsuarioAdminFormDTO dto) {
        Usuario usuario = gestionUsuariosService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable String id, @Valid @RequestBody UsuarioAdminFormDTO dto) {
        return gestionUsuariosService.actualizar(id, dto);
    }

    @PatchMapping("/{id}/estado")
    public Usuario cambiarEstado(@PathVariable String id, @RequestBody CambiarEstadoDTO dto) {
        return gestionUsuariosService.cambiarEstado(id, dto.isActivo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        gestionUsuariosService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}