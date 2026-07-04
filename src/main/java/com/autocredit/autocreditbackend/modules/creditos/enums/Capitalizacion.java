package com.autocredit.autocreditbackend.modules.creditos.enums;

import java.util.Map;

public enum Capitalizacion {
    MENSUAL(30),
    BIMESTRAL(60),
    TRIMESTRAL(90),
    SEMESTRAL(180),
    ANUAL(360);

    private final int dias;

    Capitalizacion(int dias) {
        this.dias = dias;
    }

    public int getDias() {
        return dias;
    }

    public static final Map<Capitalizacion, Integer> DIAS_MAP = Map.of(
            MENSUAL, 30, BIMESTRAL, 60, TRIMESTRAL, 90, SEMESTRAL, 180, ANUAL, 360
    );
}