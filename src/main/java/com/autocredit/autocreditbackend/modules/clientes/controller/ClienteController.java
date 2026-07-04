package com.autocredit.autocreditbackend.modules.clientes.controller;

import com.autocredit.autocreditbackend.modules.clientes.dto.ClienteFormDTO;
import com.autocredit.autocreditbackend.modules.clientes.dto.ClienteListItemDTO;
import com.autocredit.autocreditbackend.modules.clientes.entity.Cliente;
import com.autocredit.autocreditbackend.modules.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteListItemDTO>> listar(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) String estado
    ) {
        return ResponseEntity.ok(clienteService.listar(buscar, estado));
    }

    @GetMapping("/mis-datos")
    public ResponseEntity<Cliente> obtenerMisDatos() {
        return ResponseEntity.ok(clienteService.obtenerMisDatos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @GetMapping("/verificar-correo")
    public ResponseEntity<Map<String, Boolean>> verificarCorreoDuplicado(
            @RequestParam String correo,
            @RequestParam(required = false) String excluirId
    ) {
        boolean duplicado = clienteService.existeCorreoDuplicado(correo, excluirId);
        return ResponseEntity.ok(Map.of("duplicado", duplicado));
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody ClienteFormDTO dto) {
        Cliente cliente = clienteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable String id, @Valid @RequestBody ClienteFormDTO dto) {
        return ResponseEntity.ok(clienteService.actualizar(id, dto));
    }

    @PutMapping("/mis-datos")
    public ResponseEntity<Cliente> actualizarMisDatos(@Valid @RequestBody ClienteFormDTO dto) {
        return ResponseEntity.ok(clienteService.actualizarMisDatos(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}