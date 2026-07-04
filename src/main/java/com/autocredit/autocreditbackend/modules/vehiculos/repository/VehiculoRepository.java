package com.autocredit.autocreditbackend.modules.vehiculos.repository;

import com.autocredit.autocreditbackend.modules.vehiculos.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

    Optional<Vehiculo> findByClienteId(String clienteId);

    void deleteByClienteId(String clienteId);

    @org.springframework.data.jpa.repository.Query(
            "SELECT v FROM Vehiculo v WHERE " +
                    "LOWER(v.marca) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
                    "LOWER(v.modelo) LIKE LOWER(CONCAT('%', :filtro, '%'))"
    )
    List<Vehiculo> buscarPorMarcaOModelo(String filtro);
}