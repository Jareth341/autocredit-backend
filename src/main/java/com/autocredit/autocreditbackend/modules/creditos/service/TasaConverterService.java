package com.autocredit.autocreditbackend.modules.creditos.service;

import com.autocredit.autocreditbackend.modules.creditos.enums.Capitalizacion;
import com.autocredit.autocreditbackend.modules.creditos.enums.TipoTasa;
import org.springframework.stereotype.Service;

@Service
public class TasaConverterService {

    /** TEA = (1 + TNA/m)^m - 1, donde m = 360 / dias de capitalizacion */
    public double tnaATea(double tnaPorcentual, Capitalizacion capitalizacion) {
        double tna = tnaPorcentual / 100;
        int diasCap = capitalizacion.getDias();
        double m = 360.0 / diasCap;
        double tea = Math.pow(1 + tna / m, m) - 1;
        return tea * 100;
    }

    /** TEM = (1 + TEA)^(1/12) - 1 */
    public double teaATem(double teaPorcentual) {
        double tea = teaPorcentual / 100;
        double tem = Math.pow(1 + tea, 1.0 / 12) - 1;
        return tem * 100;
    }

    /** Punto de entrada unico: dado el tipo de tasa, devuelve la TEM lista para el metodo frances */
    public double calcularTem(TipoTasa tipoTasa, double valorTasa, Capitalizacion capitalizacion) {
        if (tipoTasa == TipoTasa.TNA) {
            if (capitalizacion == null) {
                throw new IllegalArgumentException("La capitalización es requerida cuando la tasa es TNA");
            }
            double tea = tnaATea(valorTasa, capitalizacion);
            return teaATem(tea);
        }
        return teaATem(valorTasa);
    }

    public double temATeaAnual(double temPorcentual) {
        double tem = temPorcentual / 100;
        double tea = Math.pow(1 + tem, 12) - 1;
        return tea * 100;
    }
}