package com.example.demo.services;

import com.example.demo.audit.AuditContext;
import com.example.demo.entities.Factura;
import com.example.demo.entities.Plan;
import com.example.demo.entities.Suscripcion;
import com.example.demo.enums.EstadoSuscripcion;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SuscripcionService {

    private final ProrrateoCalculator prorrateoCalculator;
    private final FacturaService facturaService;

    public SuscripcionService(ProrrateoCalculator prorrateoCalculator, FacturaService facturaService) {
        this.prorrateoCalculator = prorrateoCalculator;
        this.facturaService = facturaService;
    }

    public Suscripcion crearSuscripcionInicial(Plan plan, LocalDate fechaInicio) {
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setPlan(plan);
        suscripcion.setEstado(EstadoSuscripcion.ACTIVA);
        suscripcion.setFechaInicio(fechaInicio);
        suscripcion.setFechaRenovacion(fechaInicio.plusDays(30));
        return suscripcion;
    }

    public Optional<Factura> cambiarPlanConProrrateo(Suscripcion suscripcion, Plan nuevoPlan, LocalDate fechaCambio, String actor) {
        AuditContext.setActor(actor);
        try {
            BigDecimal monto = prorrateoCalculator.calcularProrrateo(
                    suscripcion.getPlan(),
                    nuevoPlan,
                    fechaCambio,
                    suscripcion.getFechaRenovacion()
            );

            suscripcion.setPlan(nuevoPlan);
            suscripcion.setUltimoCambioPlan(LocalDateTime.now());

            if (monto.compareTo(BigDecimal.ZERO) > 0) {
                return Optional.of(facturaService.generarFacturaProrrateo(suscripcion, monto, fechaCambio));
            }
            return Optional.empty();
        } finally {
            AuditContext.clear();
        }
    }

    public Optional<Factura> generarFacturaSiCorresponde(Suscripcion suscripcion, LocalDate hoy) {
        if (hoy.isBefore(suscripcion.getFechaRenovacion())) {
            return Optional.empty();
        }

        Factura factura = facturaService.generarFacturaRecurrencia(suscripcion, hoy);
        suscripcion.setFechaRenovacion(suscripcion.getFechaRenovacion().plusDays(30));
        return Optional.of(factura);
    }
}