package com.autocredit.autocreditbackend.modules.simulacion.service;

import com.autocredit.autocreditbackend.modules.creditos.enums.TipoGracia;
import com.autocredit.autocreditbackend.modules.simulacion.dto.ParametrosCronogramaDTO;
import com.autocredit.autocreditbackend.modules.simulacion.entity.Cronograma;
import com.autocredit.autocreditbackend.modules.simulacion.entity.PeriodoPago;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetodoFrancesService {

    /** R = (P - B/(1+i)^n) * i / (1 - (1+i)^-n) */
    public double calcularCuota(double montoAFinanciar, double temPorcentual, int plazoMeses, double balloon) {
        double i = temPorcentual / 100;
        if (i == 0) return (montoAFinanciar - balloon) / plazoMeses;

        double factor = Math.pow(1 + i, plazoMeses);
        double valorPresenteBalloon = balloon / factor;
        return ((montoAFinanciar - valorPresenteBalloon) * i) / (1 - Math.pow(1 + i, -plazoMeses));
    }

    public Cronograma generarCronograma(ParametrosCronogramaDTO params) {
        double montoAFinanciar = params.getMontoAFinanciar();
        double temPorcentual = params.getTemPorcentual();
        int plazoMeses = params.getPlazoMeses();
        double cuotaBalloon = params.getCuotaBalloon();
        TipoGracia tipoGracia = params.getTipoGracia();
        int mesesGracia = params.getMesesGracia();
        double seguroVehicularAnual = params.getSeguroVehicularAnual();
        double precioVehiculo = params.getPrecioVehiculo();
        double seguroDesgravamenPct = params.getSeguroDesgravamenPct();
        double comisionGastos = params.getComisionGastos();

        double i = temPorcentual / 100;
        double seguroMensualFijo = (seguroVehicularAnual / 100 * precioVehiculo) / 12;

        List<PeriodoPago> periodos = new ArrayList<>();
        double saldo = montoAFinanciar;
        int plazoEfectivoParaCuota = plazoMeses;
        double montoBaseParaCuota = montoAFinanciar;

        if (tipoGracia == TipoGracia.GRACIA_TOTAL && mesesGracia > 0) {
            montoBaseParaCuota = montoAFinanciar * Math.pow(1 + i, mesesGracia);
            plazoEfectivoParaCuota = plazoMeses - mesesGracia;
        } else if (tipoGracia == TipoGracia.GRACIA_PARCIAL && mesesGracia > 0) {
            plazoEfectivoParaCuota = plazoMeses - mesesGracia;
        }

        double cuotaNormal = calcularCuota(montoBaseParaCuota, temPorcentual, plazoEfectivoParaCuota, cuotaBalloon);

        for (int n = 1; n <= plazoMeses; n++) {
            double saldoInicial = saldo;
            double interes = saldoInicial * i;
            boolean enGracia = tipoGracia != TipoGracia.SIN_GRACIA && n <= mesesGracia;
            boolean esUltimoPeriodo = n == plazoMeses;

            double amortizacion;
            double cuotaBase;
            double saldoFinal;

            if (enGracia && tipoGracia == TipoGracia.GRACIA_TOTAL) {
                amortizacion = 0;
                cuotaBase = interes;
                saldoFinal = saldoInicial + interes;
            } else if (enGracia && tipoGracia == TipoGracia.GRACIA_PARCIAL) {
                amortizacion = 0;
                cuotaBase = interes;
                saldoFinal = saldoInicial;
            } else {
                cuotaBase = cuotaNormal;
                amortizacion = cuotaBase - interes;
                saldoFinal = saldoInicial - amortizacion;
                if (esUltimoPeriodo) {
                    amortizacion = saldoInicial - cuotaBalloon;
                    saldoFinal = cuotaBalloon;
                }
            }

            double seguro = seguroMensualFijo + (saldoInicial * seguroDesgravamenPct / 100);
            double comision = n == 1 ? comisionGastos : 0;
            double balloonPeriodo = esUltimoPeriodo ? cuotaBalloon : 0;

            double cuotaTotal;
            if (enGracia && tipoGracia == TipoGracia.GRACIA_TOTAL) {
                cuotaTotal = seguro + comision;
            } else {
                cuotaTotal = cuotaBase + seguro + comision + (esUltimoPeriodo ? balloonPeriodo : 0);
            }

            periodos.add(PeriodoPago.builder()
                    .numero(n)
                    .estado(esUltimoPeriodo ? "ULTIMA_CUOTA" : enGracia ? "GRACIA" : "NORMAL")
                    .saldoInicial(redondear(saldoInicial))
                    .interes(redondear(interes))
                    .amortizacion(redondear(amortizacion))
                    .cuotaBase(redondear(cuotaBase))
                    .seguro(redondear(seguro))
                    .comision(redondear(comision))
                    .balloon(esUltimoPeriodo ? redondear(balloonPeriodo) : 0)
                    .cuotaTotal(redondear(cuotaTotal))
                    .saldoFinal(redondear(Math.max(saldoFinal, 0)))
                    .build());

            saldo = esUltimoPeriodo ? 0 : saldoFinal;
        }

        double interesTotal = periodos.stream().mapToDouble(PeriodoPago::getInteres).sum();
        double seguroTotal = periodos.stream().mapToDouble(PeriodoPago::getSeguro).sum();
        double comisionTotal = periodos.stream().mapToDouble(PeriodoPago::getComision).sum();
        double costoTotalCredito = periodos.stream().mapToDouble(PeriodoPago::getCuotaTotal).sum();

        return Cronograma.builder()
                .montoFinanciado(redondear(montoAFinanciar))
                .cuotaMensual(redondear(cuotaNormal))
                .plazoMeses(plazoMeses)
                .tem(temPorcentual)
                .interesTotal(redondear(interesTotal))
                .seguroTotal(redondear(seguroTotal))
                .comisionTotal(redondear(comisionTotal))
                .costoTotalCredito(redondear(costoTotalCredito))
                .periodos(periodos)
                .guardado(false)
                .fechaGeneracion(LocalDateTime.now())
                .build();
    }

    private double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }
}