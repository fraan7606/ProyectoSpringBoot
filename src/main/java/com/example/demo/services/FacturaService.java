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
        return Factura.builder()
                .suscripcion(suscripcion)
                .tipoFactura(TipoFactura.RECURRENTE)
                .estado(EstadoFactura.PENDIENTE)
                .periodoInicio(suscripcion.getFechaRenovacion().minusDays(30))
                .periodoFin(suscripcion.getFechaRenovacion())
                .fechaEmision(fechaEmision)
                .monto(suscripcion.getPlan().getPrecioMensual())
                .build();
    }

    public Factura generarFacturaProrrateo(Suscripcion suscripcion, BigDecimal monto, LocalDate fechaEmision) {
        return Factura.builder()
                .suscripcion(suscripcion)
                .tipoFactura(TipoFactura.PRORRATEO)
                .estado(EstadoFactura.PENDIENTE)
                .fechaEmision(fechaEmision)
                .monto(monto)
                .build();
    }
}