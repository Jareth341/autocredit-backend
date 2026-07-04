package com.autocredit.autocreditbackend.modules.analisiscomparativo.controller;

import com.autocredit.autocreditbackend.modules.analisiscomparativo.dto.ComparacionEscenariosDTO;
import com.autocredit.autocreditbackend.modules.analisiscomparativo.entity.SimulacionGuardada;
import com.autocredit.autocreditbackend.modules.analisiscomparativo.service.ComparadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulaciones-guardadas")
@RequiredArgsConstructor
public class SimulacionGuardadaController {

    private final ComparadorService comparadorService;

    @GetMapping
    public List<SimulacionGuardada> listar(@RequestParam(required = false) String buscar) {
        return comparadorService.listar(buscar);
    }

    @GetMapping("/{id}")
    public SimulacionGuardada obtenerPorId(@PathVariable String id) {
        return comparadorService.obtenerPorId(id);
    }

    @GetMapping("/comparar")
    public ComparacionEscenariosDTO comparar(@RequestParam String idA, @RequestParam String idB) {
        return comparadorService.compararEscenarios(idA, idB);
    }

    @PostMapping("/{id}/duplicar")
    public SimulacionGuardada duplicar(@PathVariable String id) {
        return comparadorService.duplicar(id);
    }
}