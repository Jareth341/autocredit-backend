package com.autocredit.autocreditbackend.modules.vehiculos.service;

import com.autocredit.autocreditbackend.core.exception.ResourceNotFoundException;
import com.autocredit.autocreditbackend.modules.clientes.entity.Cliente;
import com.autocredit.autocreditbackend.modules.clientes.repository.ClienteRepository;
import com.autocredit.autocreditbackend.modules.vehiculos.dto.VehiculoFormDTO;
import com.autocredit.autocreditbackend.modules.vehiculos.dto.VehiculoListItemDTO;
import com.autocredit.autocreditbackend.modules.vehiculos.entity.Vehiculo;
import com.autocredit.autocreditbackend.modules.vehiculos.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public List<VehiculoListItemDTO> listar(String filtro) {
        List<Vehiculo> vehiculos = (filtro != null && !filtro.isBlank())
                ? vehiculoRepository.buscarPorMarcaOModelo(filtro)
                : vehiculoRepository.findAll();

        return vehiculos.stream().map(this::aListItem).toList();
    }

    public Vehiculo obtenerPorId(String id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado"));
    }

    public Optional<Vehiculo> obtenerPorCliente(String clienteId) {
        return vehiculoRepository.findByClienteId(clienteId);
    }

    public Vehiculo crear(VehiculoFormDTO dto) {
        Vehiculo vehiculo = mapearDesdeDTO(dto);
        vehiculo.setFechaRegistro(LocalDate.now());
        Vehiculo guardado = vehiculoRepository.save(vehiculo);

        vincularVehiculoAlCliente(dto.getClienteId(), guardado);

        return guardado;
    }

    public Vehiculo actualizar(String id, VehiculoFormDTO dto) {
        Vehiculo vehiculo = obtenerPorId(id);
        actualizarCamposDesdeDTO(vehiculo, dto);
        Vehiculo actualizado = vehiculoRepository.save(vehiculo);

        vincularVehiculoAlCliente(dto.getClienteId(), actualizado);

        return actualizado;
    }

    /** Se llama en cascada cuando se elimina un cliente (ver ClienteService) */
    public void eliminarPorCliente(String clienteId) {
        vehiculoRepository.deleteByClienteId(clienteId);
    }

    // ---- Helpers privados ----

    private void vincularVehiculoAlCliente(String clienteId, Vehiculo vehiculo) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        cliente.setVehiculoAsociado(vehiculo.getMarca() + " " + vehiculo.getModelo() + " " + vehiculo.getAnio());
        cliente.setVehiculoId(vehiculo.getId());
        clienteRepository.save(cliente);
    }

    private Vehiculo mapearDesdeDTO(VehiculoFormDTO dto) {
        return Vehiculo.builder()
                .clienteId(dto.getClienteId())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .anio(dto.getAnio())
                .version(dto.getVersion())
                .color(dto.getColor())
                .tipoVehiculo(dto.getTipoVehiculo())
                .condicion(dto.getCondicion())
                .moneda(dto.getMoneda())
                .precioVenta(dto.getPrecioVenta())
                .cuotaInicial(dto.getCuotaInicial())
                .concesionario(dto.getConcesionario())
                .vendedor(dto.getVendedor())
                .telefonoConcesionario(dto.getTelefonoConcesionario())
                .observaciones(dto.getObservaciones())
                .build();
    }

    private void actualizarCamposDesdeDTO(Vehiculo vehiculo, VehiculoFormDTO dto) {
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setVersion(dto.getVersion());
        vehiculo.setColor(dto.getColor());
        vehiculo.setTipoVehiculo(dto.getTipoVehiculo());
        vehiculo.setCondicion(dto.getCondicion());
        vehiculo.setMoneda(dto.getMoneda());
        vehiculo.setPrecioVenta(dto.getPrecioVenta());
        vehiculo.setCuotaInicial(dto.getCuotaInicial());
        vehiculo.setConcesionario(dto.getConcesionario());
        vehiculo.setVendedor(dto.getVendedor());
        vehiculo.setTelefonoConcesionario(dto.getTelefonoConcesionario());
        vehiculo.setObservaciones(dto.getObservaciones());
    }

    private VehiculoListItemDTO aListItem(Vehiculo v) {
        Cliente cliente = clienteRepository.findById(v.getClienteId()).orElse(null);
        String nombreCliente = cliente != null ? cliente.getNombres() + " " + cliente.getApellidos() : "Sin cliente";

        return new VehiculoListItemDTO(
                v.getId(), v.getClienteId(), nombreCliente, v.getMarca(), v.getModelo(),
                v.getAnio(), v.getCondicion(), v.getMoneda(), v.getPrecioVenta(),
                v.getConcesionario(), v.getFechaRegistro()
        );
    }
}