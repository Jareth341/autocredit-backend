package com.autocredit.autocreditbackend.modules.creditos.service;

import com.autocredit.autocreditbackend.core.exception.ResourceNotFoundException;
import com.autocredit.autocreditbackend.modules.clientes.entity.Cliente;
import com.autocredit.autocreditbackend.modules.clientes.enums.EstadoCliente;
import com.autocredit.autocreditbackend.modules.clientes.repository.ClienteRepository;
import com.autocredit.autocreditbackend.modules.creditos.dto.CreditoFormDTO;
import com.autocredit.autocreditbackend.modules.creditos.entity.Credito;
import com.autocredit.autocreditbackend.modules.creditos.repository.CreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditoService {

    private final CreditoRepository creditoRepository;
    private final ClienteRepository clienteRepository;

    public Credito obtenerPorId(String id) {
        return creditoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crédito no encontrado"));
    }

    public Optional<Credito> obtenerPorCliente(String clienteId) {
        return creditoRepository.findByClienteId(clienteId);
    }

    public Credito crear(CreditoFormDTO dto) {
        double montoAFinanciar = Math.max(dto.getPrecioVehiculo() - dto.getCuotaInicial(), 0);

        Credito credito = mapearDesdeDTO(dto);
        credito.setMontoAFinanciar(montoAFinanciar);
        credito.setFechaRegistro(LocalDate.now());

        Credito guardado = creditoRepository.save(credito);

        marcarClienteEnSimulacion(dto.getClienteId(), guardado.getId());

        return guardado;
    }

    public Credito actualizar(String id, CreditoFormDTO dto) {
        Credito credito = obtenerPorId(id);
        double montoAFinanciar = Math.max(dto.getPrecioVehiculo() - dto.getCuotaInicial(), 0);

        actualizarCamposDesdeDTO(credito, dto);
        credito.setMontoAFinanciar(montoAFinanciar);

        return creditoRepository.save(credito);
    }

    /** Se llama en cascada cuando se elimina un cliente */
    public void eliminarPorCliente(String clienteId) {
        creditoRepository.deleteByClienteId(clienteId);
    }

    // ---- Helpers privados ----

    private void marcarClienteEnSimulacion(String clienteId, String creditoId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        cliente.setCreditoId(creditoId);
        cliente.setEstado(EstadoCliente.EN_SIMULACION);
        clienteRepository.save(cliente);
    }

    private Credito mapearDesdeDTO(CreditoFormDTO dto) {
        return Credito.builder()
                .clienteId(dto.getClienteId())
                .vehiculoId(dto.getVehiculoId())
                .moneda(dto.getMoneda())
                .precioVehiculo(dto.getPrecioVehiculo())
                .cuotaInicial(dto.getCuotaInicial())
                .plazoMeses(dto.getPlazoMeses())
                .cuotaBalloon(dto.getCuotaBalloon() != null ? dto.getCuotaBalloon() : 0.0)
                .tipoTasa(dto.getTipoTasa())
                .valorTasa(dto.getValorTasa())
                .capitalizacion(dto.getCapitalizacion())
                .tipoGracia(dto.getTipoGracia())
                .mesesGracia(dto.getMesesGracia() != null ? dto.getMesesGracia() : 0)
                .seguroVehicularAnual(dto.getSeguroVehicularAnual() != null ? dto.getSeguroVehicularAnual() : 0.0)
                .seguroDesgravamen(dto.getSeguroDesgravamen() != null ? dto.getSeguroDesgravamen() : 0.0)
                .comisionGastos(dto.getComisionGastos() != null ? dto.getComisionGastos() : 0.0)
                .tasaDescuentoVan(dto.getTasaDescuentoVan())
                .build();
    }

    private void actualizarCamposDesdeDTO(Credito credito, CreditoFormDTO dto) {
        credito.setMoneda(dto.getMoneda());
        credito.setPrecioVehiculo(dto.getPrecioVehiculo());
        credito.setCuotaInicial(dto.getCuotaInicial());
        credito.setPlazoMeses(dto.getPlazoMeses());
        credito.setCuotaBalloon(dto.getCuotaBalloon() != null ? dto.getCuotaBalloon() : 0.0);
        credito.setTipoTasa(dto.getTipoTasa());
        credito.setValorTasa(dto.getValorTasa());
        credito.setCapitalizacion(dto.getCapitalizacion());
        credito.setTipoGracia(dto.getTipoGracia());
        credito.setMesesGracia(dto.getMesesGracia() != null ? dto.getMesesGracia() : 0);
        credito.setSeguroVehicularAnual(dto.getSeguroVehicularAnual());
        credito.setSeguroDesgravamen(dto.getSeguroDesgravamen());
        credito.setComisionGastos(dto.getComisionGastos());
        credito.setTasaDescuentoVan(dto.getTasaDescuentoVan());
    }
}