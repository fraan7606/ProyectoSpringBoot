package com.example.demo.services;

import com.example.demo.entities.Factura;
import com.example.demo.entities.Suscripcion;
import com.example.demo.enums.EstadoFactura;
import com.example.demo.enums.TipoFactura;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class FacturaService {

    public Factura generarFacturaRecurrencia(Suscripcion suscripcion, LocalDate fechaEmision) {
        BigDecimal taxRate = getTaxRateForSuscripcion(suscripcion);
        BigDecimal subtotal = suscripcion.getPlan().getPrecioMensual();
        BigDecimal impuestos = subtotal.multiply(taxRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal total = subtotal.add(impuestos);

        return Factura.builder()
                .suscripcion(suscripcion)
                .tipoFactura(TipoFactura.RECURRENTE)
                .estado(EstadoFactura.PENDIENTE)
                .periodoInicio(suscripcion.getFechaRenovacion().minusDays(30))
                .periodoFin(suscripcion.getFechaRenovacion())
                .fechaEmision(fechaEmision)
                .montoSubtotal(subtotal)
                .montoImpuestos(impuestos)
                .monto(total)
                .build();
    }

    public Factura generarFacturaProrrateo(Suscripcion suscripcion, BigDecimal montoProrrateo, LocalDate fechaEmision) {
        BigDecimal taxRate = getTaxRateForSuscripcion(suscripcion);
        BigDecimal impuestos = montoProrrateo.multiply(taxRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal total = montoProrrateo.add(impuestos);

        return Factura.builder()
                .suscripcion(suscripcion)
                .tipoFactura(TipoFactura.PRORRATEO)
                .estado(EstadoFactura.PENDIENTE)
                .fechaEmision(fechaEmision)
                .montoSubtotal(montoProrrateo)
                .montoImpuestos(impuestos)
                .monto(total)
                .build();
    }

    private BigDecimal getTaxRateForSuscripcion(Suscripcion suscripcion) {
        if (suscripcion.getUsuario() == null || suscripcion.getUsuario().getPerfil() == null) {
            return new BigDecimal("0.21"); // Default IVA 21%
        }

        String pais = suscripcion.getUsuario().getPerfil().getPais();
        if (pais == null)
            return new BigDecimal("0.21");

        return switch (pais.toUpperCase()) {
            case "ESPAÑA", "SPAIN" -> new BigDecimal("0.21");
            case "ARGENTINA" -> new BigDecimal("0.21");
            case "ALEMANIA", "GERMANY" -> new BigDecimal("0.19");
            case "FRANCIA", "FRANCE" -> new BigDecimal("0.20");
            case "MÉXICO", "MEXICO" -> new BigDecimal("0.16");
            case "USA", "EEUU", "UNITED STATES" -> new BigDecimal("0.00");
            default -> new BigDecimal("0.21");
        };
    }
}