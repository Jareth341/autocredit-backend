package com.autocredit.autocreditbackend.modules.simulacion.service;

import com.autocredit.autocreditbackend.core.exception.ResourceNotFoundException;
import com.autocredit.autocreditbackend.modules.clientes.entity.Cliente;
import com.autocredit.autocreditbackend.modules.clientes.enums.EstadoCliente;
import com.autocredit.autocreditbackend.modules.clientes.repository.ClienteRepository;
import com.autocredit.autocreditbackend.modules.creditos.entity.Credito;
import com.autocredit.autocreditbackend.modules.creditos.repository.CreditoRepository;
import com.autocredit.autocreditbackend.modules.creditos.service.TasaConverterService;
import com.autocredit.autocreditbackend.modules.simulacion.dto.ParametrosCronogramaDTO;
import com.autocredit.autocreditbackend.modules.simulacion.entity.Cronograma;
import com.autocredit.autocreditbackend.modules.simulacion.repository.CronogramaRepository;
import com.autocredit.autocreditbackend.modules.analisiscomparativo.service.ComparadorService;
import com.autocredit.autocreditbackend.modules.creditos.service.TasaConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimulacionService {

    private final MetodoFrancesService metodoFrancesService;
    private final TasaConverterService tasaConverterService;
    private final CronogramaRepository cronogramaRepository;
    private final CreditoRepository creditoRepository;
    private final ClienteRepository clienteRepository;
    private final ComparadorService comparadorService;

    public Cronograma generarCronogramaDeCredito(String creditoId) {
        Credito credito = creditoRepository.findById(creditoId)
                .orElseThrow(() -> new ResourceNotFoundException("Crédito no encontrado"));

        double tem = tasaConverterService.calcularTem(
                credito.getTipoTasa(), credito.getValorTasa(), credito.getCapitalizacion()
        );

        ParametrosCronogramaDTO params = new ParametrosCronogramaDTO();
        params.setMontoAFinanciar(credito.getMontoAFinanciar());
        params.setTemPorcentual(tem);
        params.setPlazoMeses(credito.getPlazoMeses());
        params.setCuotaBalloon(credito.getCuotaBalloon() != null ? credito.getCuotaBalloon() : 0);
        params.setTipoGracia(credito.getTipoGracia());
        params.setMesesGracia(credito.getMesesGracia() != null ? credito.getMesesGracia() : 0);
        params.setSeguroVehicularAnual(credito.getSeguroVehicularAnual() != null ? credito.getSeguroVehicularAnual() : 0);
        params.setPrecioVehiculo(credito.getPrecioVehiculo());
        params.setSeguroDesgravamenPct(credito.getSeguroDesgravamen() != null ? credito.getSeguroDesgravamen() : 0);
        params.setComisionGastos(credito.getComisionGastos() != null ? credito.getComisionGastos() : 0);

        Cronograma cronograma = metodoFrancesService.generarCronograma(params);
        cronograma.setCreditoId(creditoId);

        return cronograma;
    }


    // Reemplazar el metodo guardarSimulacion() por:
    public Cronograma guardarSimulacion(Cronograma cronograma) {
        Cronograma guardado = cronogramaRepository.save(cronograma);
        guardado.setGuardado(true);
        cronogramaRepository.save(guardado);

        marcarClienteConCreditoActivo(cronograma.getCreditoId());
        sincronizarConAnalisisComparativo(guardado);

        return guardado;
    }

    private void sincronizarConAnalisisComparativo(Cronograma cronograma) {
        Credito credito = creditoRepository.findById(cronograma.getCreditoId()).orElse(null);
        if (credito == null) return;

        Cliente cliente = clienteRepository.findById(credito.getClienteId()).orElse(null);
        if (cliente == null) return;

        double tirEstimada = new IndicadoresFinancierosService().calcularTirMensual(cronograma);
        double tceaEstimada = new IndicadoresFinancierosService().tirMensualATirAnual(tirEstimada);
        double porcentajeCuotaIngreso = (cronograma.getCuotaMensual() / cliente.getIngresoMensual()) * 100;

        comparadorService.sincronizarDesdeCredito(
                cliente.getId(), credito.getId(), cliente.getNombres() + " " + cliente.getApellidos(),
                cliente.getEntidad() != null ? cliente.getEntidad() : "AutoCredit",
                credito.getMoneda(), cronograma.getCuotaMensual(), tceaEstimada, tirEstimada,
                cronograma.getCostoTotalCredito(), cronograma.getPlazoMeses(),
                credito.getTipoTasa(), credito.getValorTasa(), porcentajeCuotaIngreso
        );
    }

    public Optional<Cronograma> obtenerPorCredito(String creditoId) {
        return cronogramaRepository.findByCreditoId(creditoId);
    }

    /** Se llama en cascada cuando se elimina un cliente */
    public void eliminarPorCredito(String creditoId) {
        cronogramaRepository.deleteByCreditoId(creditoId);
    }

    private void marcarClienteConCreditoActivo(String creditoId) {
        Credito credito = creditoRepository.findById(creditoId).orElse(null);
        if (credito == null) return;

        Cliente cliente = clienteRepository.findById(credito.getClienteId()).orElse(null);
        if (cliente == null) return;

        cliente.setEstado(EstadoCliente.CREDITO_ACTIVO);
        clienteRepository.save(cliente);
    }
}