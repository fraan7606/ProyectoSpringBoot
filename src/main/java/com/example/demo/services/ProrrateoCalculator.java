package com.example.demo.services;

import com.example.demo.entities.Plan;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ProrrateoCalculator {

    public BigDecimal calcularProrrateo(Plan planActual, Plan nuevoPlan, LocalDate fechaCambio, LocalDate fechaRenovacion) {
        if (planActual == null || nuevoPlan == null || fechaCambio == null || fechaRenovacion == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (nuevoPlan.getPrecioMensual().compareTo(planActual.getPrecioMensual()) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        long diasRestantes = ChronoUnit.DAYS.between(fechaCambio, fechaRenovacion);
        if (diasRestantes <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal diferencia = nuevoPlan.getPrecioMensual().subtract(planActual.getPrecioMensual());
        BigDecimal factor = BigDecimal.valueOf(diasRestantes)
                .divide(BigDecimal.valueOf(30), 6, RoundingMode.HALF_UP);

        return diferencia.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}