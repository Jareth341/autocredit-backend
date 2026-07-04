package com.autocredit.autocreditbackend.modules.analisiscomparativo.repository;

import com.autocredit.autocreditbackend.modules.analisiscomparativo.entity.SimulacionGuardada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SimulacionGuardadaRepository extends JpaRepository<SimulacionGuardada, String> {

    boolean existsByCreditoId(String creditoId);

    void deleteByCreditoId(String creditoId);

    List<SimulacionGuardada> findByClienteNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(
            String clienteNombre, String codigo
    );

    Optional<SimulacionGuardada> findByCreditoId(String creditoId);
}