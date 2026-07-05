package com.autocredit.autocreditbackend.modules.clientes.service;

import com.autocredit.autocreditbackend.core.exception.DuplicateResourceException;
import com.autocredit.autocreditbackend.core.exception.ResourceNotFoundException;
import com.autocredit.autocreditbackend.modules.autenticacion.entity.Usuario;
import com.autocredit.autocreditbackend.modules.autenticacion.enums.Rol;
import com.autocredit.autocreditbackend.modules.clientes.dto.ClienteFormDTO;
import com.autocredit.autocreditbackend.modules.clientes.dto.ClienteListItemDTO;
import com.autocredit.autocreditbackend.modules.clientes.entity.Cliente;
import com.autocredit.autocreditbackend.modules.clientes.enums.EstadoCliente;
import com.autocredit.autocreditbackend.modules.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.autocredit.autocreditbackend.modules.vehiculos.service.VehiculoService;
import com.autocredit.autocreditbackend.modules.creditos.service.CreditoService;
import com.autocredit.autocreditbackend.modules.simulacion.service.SimulacionService;
import com.autocredit.autocreditbackend.modules.analisiscomparativo.service.ComparadorService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VehiculoService vehiculoService;
    private final CreditoService creditoService;
    private final SimulacionService simulacionService;
    private final ComparadorService comparadorService;
    private Rol obtenerRolUsuarioActual() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario.getRol();
    }

    public List<ClienteListItemDTO> listar(String filtro, String estado) {
        List<Cliente> clientes;

        if (filtro != null && !filtro.isBlank()) {
            clientes = clienteRepository
                    .findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                            filtro, filtro, filtro, filtro
                    );
        } else if (estado != null && !estado.isBlank()) {
            clientes = clienteRepository.findByEstado(EstadoCliente.valueOf(estado));
        } else {
            clientes = clienteRepository.findAll();
        }

        // Solo el Asesor financiero ve unicamente sus propios clientes.
        // El Administrador (u otro rol con visibilidad total) ve todos.
        if (obtenerRolUsuarioActual() == Rol.ASESOR_FINANCIERO) {
            String idUsuarioActual = obtenerIdUsuarioActual();
            clientes = clientes.stream()
                    .filter(c -> c.getAsesorId().equals(idUsuarioActual))
                    .toList();
        }

        return clientes.stream().map(this::aListItem).toList();
    }

    public Cliente obtenerPorId(String id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    public Cliente obtenerMisDatos() {
        String correoUsuarioActual = obtenerCorreoUsuarioActual();
        return clienteRepository.findByCorreo(correoUsuarioActual)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró un cliente asociado a tu cuenta. Contacta a un asesor."
                ));
    }

    public Cliente crear(ClienteFormDTO dto) {
        if (clienteRepository.existsByCorreo(dto.getCorreo())) {
            throw new DuplicateResourceException("CORREO_DUPLICADO");
        }

        Cliente cliente = mapearDesdeDTO(dto);
        cliente.setEstado(EstadoCliente.RECIEN_REGISTRADO);
        cliente.setAsesorId(obtenerIdUsuarioActual());
        cliente.setFechaRegistro(LocalDate.now());

        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(String id, ClienteFormDTO dto) {
        Cliente cliente = obtenerPorId(id);

        if (clienteRepository.existsByCorreoAndIdNot(dto.getCorreo(), id)) {
            throw new DuplicateResourceException("CORREO_DUPLICADO");
        }

        actualizarCamposDesdeDTO(cliente, dto);
        cliente.setFechaActualizacion(LocalDate.now());

        return clienteRepository.save(cliente);
    }

    public Cliente actualizarMisDatos(ClienteFormDTO dto) {
        Cliente cliente = obtenerMisDatos();
        actualizarCamposDesdeDTO(cliente, dto);
        cliente.setFechaActualizacion(LocalDate.now());
        return clienteRepository.save(cliente);
    }

    public void eliminar(String id) {
        Cliente cliente = obtenerPorId(id);
        if (cliente.getCreditoId() != null) {
            simulacionService.eliminarPorCredito(cliente.getCreditoId());
            comparadorService.eliminarPorCredito(cliente.getCreditoId());
        }
        vehiculoService.eliminarPorCliente(id);
        creditoService.eliminarPorCliente(id);
        clienteRepository.delete(cliente);
    }
    public boolean existeCorreoDuplicado(String correo, String idExcluir) {
        return idExcluir != null
                ? clienteRepository.existsByCorreoAndIdNot(correo, idExcluir)
                : clienteRepository.existsByCorreo(correo);
    }

    // ---- Helpers privados ----

    private Cliente mapearDesdeDTO(ClienteFormDTO dto) {
        return Cliente.builder()
                .tipoDocumento(dto.getTipoDocumento())
                .numeroDocumento(dto.getNumeroDocumento())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .estadoCivil(dto.getEstadoCivil())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .situacionLaboral(dto.getSituacionLaboral())
                .ingresoMensual(dto.getIngresoMensual())
                .observaciones(dto.getObservaciones())
                .build();
    }

    private void actualizarCamposDesdeDTO(Cliente cliente, ClienteFormDTO dto) {
        cliente.setTipoDocumento(dto.getTipoDocumento());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setGenero(dto.getGenero());
        cliente.setEstadoCivil(dto.getEstadoCivil());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());
        cliente.setSituacionLaboral(dto.getSituacionLaboral());
        cliente.setIngresoMensual(dto.getIngresoMensual());
        cliente.setObservaciones(dto.getObservaciones());
    }

    private ClienteListItemDTO aListItem(Cliente c) {
        return new ClienteListItemDTO(
                c.getId(), c.getNombres(), c.getApellidos(), c.getNumeroDocumento(),
                c.getCorreo(), c.getTelefono(), c.getVehiculoAsociado(),
                c.getIngresoMensual(), c.getEstado(), c.getFechaRegistro()
        );
    }

    private String obtenerIdUsuarioActual() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario.getId();
    }

    private String obtenerCorreoUsuarioActual() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario.getCorreo();
    }
}