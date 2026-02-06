package com.example.demo.services;

import com.example.demo.entities.Plan;
import com.example.demo.enums.TipoPlan;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProrrateoCalculatorTest {

    private final ProrrateoCalculator calculator = new ProrrateoCalculator();

    @Test
    void calculaProrrateoCuandoUpgrade() {
        Plan basic = Plan.builder()
                .tipoPlan(TipoPlan.BASIC)
                .nombre("Basic")
                .precioMensual(new BigDecimal("10.00"))
                .build();

        Plan premium = Plan.builder()
                .tipoPlan(TipoPlan.PREMIUM)
                .nombre("Premium")
                .precioMensual(new BigDecimal("30.00"))
                .build();

        LocalDate cambio = LocalDate.of(2026, 2, 6);
        LocalDate renovacion = LocalDate.of(2026, 2, 21);

        BigDecimal prorrateo = calculator.calcularProrrateo(basic, premium, cambio, renovacion);
        assertEquals(new BigDecimal("10.00"), prorrateo);
    }

    @Test
    void noProrrateaCuandoDowngrade() {
        Plan premium = Plan.builder()
                .tipoPlan(TipoPlan.PREMIUM)
                .nombre("Premium")
                .precioMensual(new BigDecimal("30.00"))
                .build();

        Plan basic = Plan.builder()
                .tipoPlan(TipoPlan.BASIC)
                .nombre("Basic")
                .precioMensual(new BigDecimal("10.00"))
                .build();

        LocalDate cambio = LocalDate.of(2026, 2, 6);
        LocalDate renovacion = LocalDate.of(2026, 2, 21);

        BigDecimal prorrateo = calculator.calcularProrrateo(premium, basic, cambio, renovacion);
        assertEquals(new BigDecimal("0.00"), prorrateo);
    }
}