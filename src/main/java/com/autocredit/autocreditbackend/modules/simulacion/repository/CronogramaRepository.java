package com.autocredit.autocreditbackend.modules.simulacion.repository;

import com.autocredit.autocreditbackend.modules.simulacion.entity.Cronograma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CronogramaRepository extends JpaRepository<Cronograma, String> {
    Optional<Cronograma> findByCreditoId(String creditoId);
    void deleteByCreditoId(String creditoId);
}