package com.autocredit.autocreditbackend.modules.clientes.repository;

import com.autocredit.autocreditbackend.modules.clientes.entity.Cliente;
import com.autocredit.autocreditbackend.modules.clientes.enums.EstadoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    Optional<Cliente> findByCorreo(String correo);

    boolean existsByCorreoAndIdNot(String correo, String id);

    boolean existsByCorreo(String correo);

    List<Cliente> findByEstado(EstadoCliente estado);

    List<Cliente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrNumeroDocumentoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
            String nombres, String apellidos, String numeroDocumento, String correo
    );
}