package com.autocredit.autocreditbackend.modules.clientes.entity;

import com.autocredit.autocreditbackend.modules.clientes.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false, unique = true)
    private String numeroDocumento;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;

    private String direccion;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false, unique = true)
    private String correo;

    @Enumerated(EnumType.STRING)
    private SituacionLaboral situacionLaboral;

    @Column(nullable = false)
    private Double ingresoMensual;

    @Column(length = 1000)
    private String observaciones;

    private String vehiculoAsociado;
    private String vehiculoId;
    private String creditoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCliente estado;

    @Column(nullable = false)
    private String asesorId;

    private String entidad;

    private LocalDate fechaRegistro;
    private LocalDate fechaActualizacion;
}