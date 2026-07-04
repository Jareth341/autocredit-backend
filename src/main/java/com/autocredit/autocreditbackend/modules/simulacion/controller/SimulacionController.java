package com.autocredit.autocreditbackend.modules.simulacion.controller;

import com.autocredit.autocreditbackend.modules.simulacion.entity.Cronograma;
import com.autocredit.autocreditbackend.modules.simulacion.service.SimulacionService;
import com.autocredit.autocreditbackend.modules.simulacion.dto.IndicadoresFinancierosDTO;
import com.autocredit.autocreditbackend.modules.simulacion.service.IndicadoresFinancierosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulaciones")
@RequiredArgsConstructor
public class SimulacionController {

    private final SimulacionService simulacionService;
    private final IndicadoresFinancierosService indicadoresFinancierosService;

    @GetMapping("/generar/{creditoId}")
    public ResponseEntity<Cronograma> generar(@PathVariable String creditoId) {
        return ResponseEntity.ok(simulacionService.generarCronogramaDeCredito(creditoId));
    }

    @PostMapping
    public ResponseEntity<Cronograma> guardar(@RequestBody Cronograma cronograma) {
        return ResponseEntity.ok(simulacionService.guardarSimulacion(cronograma));
    }

    @GetMapping("/por-credito/{creditoId}")
    public ResponseEntity<Cronograma> obtenerPorCredito(@PathVariable String creditoId) {
        return simulacionService.obtenerPorCredito(creditoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @GetMapping("/{creditoId}/indicadores")
    public ResponseEntity<IndicadoresFinancierosDTO> obtenerIndicadores(
            @PathVariable String creditoId,
            @RequestParam double tasaDescuentoVan
    ) {
        Cronograma cronograma = simulacionService.generarCronogramaDeCredito(creditoId);
        IndicadoresFinancierosDTO indicadores = indicadoresFinancierosService.calcularIndicadoresCompletos(cronograma, tasaDescuentoVan);
        return ResponseEntity.ok(indicadores);
    }
}