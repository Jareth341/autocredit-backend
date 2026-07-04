package com.autocredit.autocreditbackend.modules.simulacion.service;

import com.autocredit.autocreditbackend.modules.simulacion.dto.IndicadoresFinancierosDTO;
import com.autocredit.autocreditbackend.modules.simulacion.entity.Cronograma;
import com.autocredit.autocreditbackend.modules.simulacion.entity.PeriodoPago;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndicadoresFinancierosService {

    private List<Double> construirFlujoCaja(Cronograma cronograma) {
        List<Double> flujo = new ArrayList<>();
        flujo.add(cronograma.getMontoFinanciado());
        for (PeriodoPago periodo : cronograma.getPeriodos()) {
            flujo.add(-periodo.getCuotaTotal());
        }
        return flujo;
    }

    public double calcularVan(Cronograma cronograma, double tasaDescuentoAnualPct) {
        List<Double> flujo = construirFlujoCaja(cronograma);
        double tasaMensual = Math.pow(1 + tasaDescuentoAnualPct / 100, 1.0 / 12) - 1;

        double van = 0;
        for (int periodo = 0; periodo < flujo.size(); periodo++) {
            van += flujo.get(periodo) / Math.pow(1 + tasaMensual, periodo);
        }
        return Math.round(van * 100) / 100.0;
    }

    public double calcularTirMensual(Cronograma cronograma) {
        List<Double> flujo = construirFlujoCaja(cronograma);
        double tasa = 0.02;
        int maxIteraciones = 200;
        double tolerancia = 1e-7;

        for (int iter = 0; iter < maxIteraciones; iter++) {
            double van = 0;
            double derivada = 0;

            for (int periodo = 0; periodo < flujo.size(); periodo++) {
                double valor = flujo.get(periodo);
                van += valor / Math.pow(1 + tasa, periodo);
                if (periodo > 0) {
                    derivada -= (periodo * valor) / Math.pow(1 + tasa, periodo + 1);
                }
            }

            if (Math.abs(van) < tolerancia || derivada == 0) break;
            tasa = tasa - van / derivada;
        }

        return Math.round(tasa * 10000) / 100.0;
    }

    public double tirMensualATirAnual(double tirMensualPct) {
        double tem = tirMensualPct / 100;
        double tea = Math.pow(1 + tem, 12) - 1;
        return Math.round(tea * 10000) / 100.0;
    }

    public double calcularTcea(Cronograma cronograma) {
        double tirMensual = calcularTirMensual(cronograma);
        return tirMensualATirAnual(tirMensual);
    }

    public IndicadoresFinancierosDTO calcularIndicadoresCompletos(Cronograma cronograma, double tasaDescuentoAnualPct) {
        double tirMensual = calcularTirMensual(cronograma);
        return new IndicadoresFinancierosDTO(
                calcularVan(cronograma, tasaDescuentoAnualPct),
                tirMensual,
                tirMensualATirAnual(tirMensual),
                calcularTcea(cronograma),
                cronograma.getInteresTotal(),
                cronograma.getSeguroTotal(),
                cronograma.getComisionTotal(),
                cronograma.getCostoTotalCredito()
        );
    }
}