package com.example.demo.services;

import com.example.demo.entities.Factura;
import com.example.demo.entities.Plan;
import com.example.demo.entities.Suscripcion;
import com.example.demo.enums.TipoPlan;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuscripcionServiceTest {

        private final SuscripcionService service = new SuscripcionService(new ProrrateoCalculator(),
                        new FacturaService());

        @Test
        void generaFacturaSiCorresponde() {
                Plan basic = Plan.builder()
                                .tipoPlan(TipoPlan.BASIC)
                                .nombre("Basic")
                                .precioMensual(new BigDecimal("15.00"))
                                .build();

                Suscripcion suscripcion = service.crearSuscripcionInicial(basic, LocalDate.of(2026, 1, 7));

                var facturaOpt = service.generarFacturaSiCorresponde(suscripcion, LocalDate.of(2026, 2, 6));

                assertTrue(facturaOpt.isPresent());
                Factura factura = facturaOpt.get();
                assertEquals(new BigDecimal("18.15"), factura.getMonto());
        }

        @Test
        void cambioDePlanCaroGeneraProrrateo() {
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

                Suscripcion suscripcion = service.crearSuscripcionInicial(basic, LocalDate.of(2026, 2, 1));

                var facturaOpt = service.cambiarPlanConProrrateo(
                                suscripcion,
                                premium,
                                LocalDate.of(2026, 2, 6),
                                "user@example.com");

                assertTrue(facturaOpt.isPresent());
                assertEquals(new BigDecimal("20.17"), facturaOpt.get().getMonto());
                assertEquals(premium, suscripcion.getPlan());
        }
}