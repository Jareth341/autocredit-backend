package com.autocredit.autocreditbackend.modules.creditos.repository;

import com.autocredit.autocreditbackend.modules.creditos.entity.Credito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditoRepository extends JpaRepository<Credito, String> {

    Optional<Credito> findByClienteId(String clienteId);

    void deleteByClienteId(String clienteId);
}