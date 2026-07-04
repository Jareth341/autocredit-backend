package com.autocredit.autocreditbackend.modules.alertasriesgo.service;

import com.autocredit.autocreditbackend.modules.alertasriesgo.dto.AlertaFinancieraDTO;
import com.autocredit.autocreditbackend.modules.alertasriesgo.dto.DatosParaAlertasDTO;
import com.autocredit.autocreditbackend.modules.creditos.enums.TipoGracia;
import com.autocredit.autocreditbackend.modules.vehiculos.enums.MonedaVehiculo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertasService {

    private static final double LIMITE_CUOTA_INGRESO_PCT = 35;
    private static final double LIMITE_BALLOON_MONTO_PCT = 20;

    public List<AlertaFinancieraDTO> evaluarAlertas(DatosParaAlertasDTO datos) {
        List<AlertaFinancieraDTO> alertas = new ArrayList<>();

        alertas.add(evaluarCuotaIngreso(datos));
        alertas.add(evaluarBalloon(datos));
        alertas.add(evaluarRiesgoCambiario(datos));

        AlertaFinancieraDTO alertaGracia = evaluarGracia(datos);
        if (alertaGracia != null) alertas.add(alertaGracia);

        return alertas;
    }

    private AlertaFinancieraDTO evaluarCuotaIngreso(DatosParaAlertasDTO datos) {
        double porcentaje = (datos.getCuotaMensual() / datos.getIngresoMensualCliente()) * 100;

        if (porcentaje > LIMITE_CUOTA_INGRESO_PCT) {
            return new AlertaFinancieraDTO(
                    "WARN",
                    String.format("Cuota representa el %.1f%% del ingreso mensual", porcentaje),
                    String.format("La cuota (%.2f) frente al ingreso declarado supera el %.0f%% recomendado. Se sugiere evaluar un plazo mayor o una cuota inicial más alta.",
                            datos.getCuotaMensual(), LIMITE_CUOTA_INGRESO_PCT)
            );
        }
        return new AlertaFinancieraDTO(
                "OK",
                "Cuota dentro del rango saludable",
                String.format("La cuota mensual representa el %.1f%% del ingreso declarado, dentro del límite recomendado.", porcentaje)
        );
    }

    private AlertaFinancieraDTO evaluarBalloon(DatosParaAlertasDTO datos) {
        if (datos.getCuotaBalloon() <= 0) {
            return new AlertaFinancieraDTO(
                    "OK",
                    "Sin cuota balloon",
                    "Esta simulación no tiene pago final balloon, por lo que no existe riesgo de concentración de deuda al final del plazo."
            );
        }

        double porcentaje = (datos.getCuotaBalloon() / datos.getMontoFinanciado()) * 100;
        if (porcentaje > LIMITE_BALLOON_MONTO_PCT) {
            return new AlertaFinancieraDTO(
                    "WARN",
                    "Cuota final balloon elevada",
                    String.format("Tu cuota final de %.2f representa el %.1f%% del monto financiado. Planifica con anticipación.",
                            datos.getCuotaBalloon(), porcentaje)
            );
        }
        return new AlertaFinancieraDTO(
                "OK",
                "Balloon dentro de rango razonable",
                String.format("El balloon representa el %.1f%% del monto financiado.", porcentaje)
        );
    }

    private AlertaFinancieraDTO evaluarRiesgoCambiario(DatosParaAlertasDTO datos) {
        if (datos.getMonedaCredito() != MonedaVehiculo.PEN) {
            return new AlertaFinancieraDTO(
                    "WARN",
                    "Riesgo cambiario",
                    "El crédito está en una moneda distinta a los ingresos habituales del cliente. Las variaciones del tipo de cambio pueden afectar la cuota."
            );
        }
        return new AlertaFinancieraDTO(
                "OK",
                "Sin riesgo cambiario",
                "El crédito está en la misma moneda que el ingreso declarado del cliente, por lo que no aplica riesgo por tipo de cambio."
        );
    }

    private AlertaFinancieraDTO evaluarGracia(DatosParaAlertasDTO datos) {
        if (datos.getTipoGracia() == TipoGracia.SIN_GRACIA || datos.getMesesGracia() <= 0) return null;

        String tipoLabel = datos.getTipoGracia() == TipoGracia.GRACIA_TOTAL ? "gracia total" : "gracia parcial";
        String motivo = datos.getTipoGracia() == TipoGracia.GRACIA_TOTAL
                ? "los intereses se capitalizan al saldo"
                : "no se reduce el saldo durante ese periodo";

        return new AlertaFinancieraDTO(
                "WARN",
                "Efecto del periodo de gracia",
                String.format("Los %d meses de %s incrementan el costo total del crédito, ya que %s.",
                        datos.getMesesGracia(), tipoLabel, motivo)
        );
    }
}