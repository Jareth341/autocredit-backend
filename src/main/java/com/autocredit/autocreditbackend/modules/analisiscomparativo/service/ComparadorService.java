package com.autocredit.autocreditbackend.modules.analisiscomparativo.service;

import com.autocredit.autocreditbackend.core.exception.ResourceNotFoundException;
import com.autocredit.autocreditbackend.modules.analisiscomparativo.dto.ComparacionEscenariosDTO;
import com.autocredit.autocreditbackend.modules.analisiscomparativo.entity.SimulacionGuardada;
import com.autocredit.autocreditbackend.modules.analisiscomparativo.repository.SimulacionGuardadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ComparadorService {

    private final SimulacionGuardadaRepository repository;

    public List<SimulacionGuardada> listar(String filtro) {
        if (filtro != null && !filtro.isBlank()) {
            return repository.findByClienteNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(filtro, filtro);
        }
        return repository.findAll();
    }

    public SimulacionGuardada obtenerPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Simulación no encontrada"));
    }

    public ComparacionEscenariosDTO compararEscenarios(String idA, String idB) {
        SimulacionGuardada escenarioA = obtenerPorId(idA);
        SimulacionGuardada escenarioB = obtenerPorId(idB);

        String mejorOpcionId = escenarioA.getTcea() <= escenarioB.getTcea() ? escenarioA.getId() : escenarioB.getId();

        return new ComparacionEscenariosDTO(escenarioA, escenarioB, mejorOpcionId);
    }

    public SimulacionGuardada duplicar(String id) {
        SimulacionGuardada original = obtenerPorId(id);

        SimulacionGuardada copia = SimulacionGuardada.builder()
                .codigo("#SIM-" + (1000 + new Random().nextInt(9000)))
                .clienteId(original.getClienteId())
                .creditoId(original.getCreditoId())
                .clienteNombre(original.getClienteNombre())
                .entidad(original.getEntidad())
                .moneda(original.getMoneda())
                .cuotaMensual(original.getCuotaMensual())
                .tcea(original.getTcea())
                .tirMensual(original.getTirMensual())
                .costoTotalCredito(original.getCostoTotalCredito())
                .plazoMeses(original.getPlazoMeses())
                .nivelRiesgo(original.getNivelRiesgo())
                .tipoTasa(original.getTipoTasa())
                .valorTasa(original.getValorTasa())
                .fecha(java.time.LocalDate.now())
                .estado("GUARDADA")
                .build();

        return repository.save(copia);
    }

    /** Se llama automaticamente cuando el asesor guarda una simulacion (ver SimulacionService) */
    public void sincronizarDesdeCredito(
            String clienteId, String creditoId, String clienteNombre, String entidad,
            com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo moneda,
            double cuotaMensual, double tcea, double tirMensual, double costoTotalCredito,
            int plazoMeses, com.autocredit.autocreditbackend.modules.creditos.enums.TipoTasa tipoTasa,
            double valorTasa, double porcentajeCuotaIngreso
    ) {
        if (repository.existsByCreditoId(creditoId)) return;

        String nivelRiesgo = porcentajeCuotaIngreso > 35 ? "ALTO" : porcentajeCuotaIngreso > 25 ? "MODERADO" : "BAJO";

        SimulacionGuardada nueva = SimulacionGuardada.builder()
                .codigo("#SIM-" + (1000 + new Random().nextInt(9000)))
                .clienteId(clienteId)
                .creditoId(creditoId)
                .clienteNombre(clienteNombre)
                .entidad(entidad)
                .moneda(moneda)
                .cuotaMensual(cuotaMensual)
                .tcea(tcea)
                .tirMensual(tirMensual)
                .costoTotalCredito(costoTotalCredito)
                .plazoMeses(plazoMeses)
                .nivelRiesgo(nivelRiesgo)
                .tipoTasa(tipoTasa)
                .valorTasa(valorTasa)
                .fecha(java.time.LocalDate.now())
                .estado("GUARDADA")
                .build();

        repository.save(nueva);
    }

    public void eliminarPorCredito(String creditoId) {
        repository.deleteByCreditoId(creditoId);
    }
}