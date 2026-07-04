package com.autocredit.autocreditbackend.modules.alertasriesgo.controller;

import com.autocredit.autocreditbackend.modules.alertasriesgo.dto.AlertaFinancieraDTO;
import com.autocredit.autocreditbackend.modules.alertasriesgo.dto.DatosParaAlertasDTO;
import com.autocredit.autocreditbackend.modules.alertasriesgo.service.AlertasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertasController {

    private final AlertasService alertasService;

    @PostMapping("/evaluar")
    public ResponseEntity<List<AlertaFinancieraDTO>> evaluar(@RequestBody DatosParaAlertasDTO datos) {
        return ResponseEntity.ok(alertasService.evaluarAlertas(datos));
    }
}