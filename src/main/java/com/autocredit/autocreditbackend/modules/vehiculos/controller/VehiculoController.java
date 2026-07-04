package com.autocredit.autocreditbackend.modules.vehiculos.controller;

import com.autocredit.autocreditbackend.modules.vehiculos.dto.VehiculoFormDTO;
import com.autocredit.autocreditbackend.modules.vehiculos.dto.VehiculoListItemDTO;
import com.autocredit.autocreditbackend.modules.vehiculos.entity.Vehiculo;
import com.autocredit.autocreditbackend.modules.vehiculos.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    public ResponseEntity<List<VehiculoListItemDTO>> listar(@RequestParam(required = false) String buscar) {
        return ResponseEntity.ok(vehiculoService.listar(buscar));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }

    @GetMapping("/por-cliente/{clienteId}")
    public ResponseEntity<Vehiculo> obtenerPorCliente(@PathVariable String clienteId) {
        return vehiculoService.obtenerPorCliente(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody VehiculoFormDTO dto) {
        Vehiculo vehiculo = vehiculoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable String id, @Valid @RequestBody VehiculoFormDTO dto) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, dto));
    }
}