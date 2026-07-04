package com.autocredit.autocreditbackend.modules.creditos.controller;

import com.autocredit.autocreditbackend.modules.creditos.dto.CreditoFormDTO;
import com.autocredit.autocreditbackend.modules.creditos.entity.Credito;
import com.autocredit.autocreditbackend.modules.creditos.service.CreditoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/creditos")
@RequiredArgsConstructor
public class CreditoController {

    private final CreditoService creditoService;

    @GetMapping("/{id}")
    public ResponseEntity<Credito> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(creditoService.obtenerPorId(id));
    }

    @GetMapping("/por-cliente/{clienteId}")
    public ResponseEntity<Credito> obtenerPorCliente(@PathVariable String clienteId) {
        return creditoService.obtenerPorCliente(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PostMapping
    public ResponseEntity<Credito> crear(@Valid @RequestBody CreditoFormDTO dto) {
        Credito credito = creditoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(credito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Credito> actualizar(@PathVariable String id, @Valid @RequestBody CreditoFormDTO dto) {
        return ResponseEntity.ok(creditoService.actualizar(id, dto));
    }
}