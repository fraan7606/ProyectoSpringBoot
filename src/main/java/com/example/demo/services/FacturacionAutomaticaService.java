package com.example.demo.services;

import com.example.demo.entities.Factura;
import com.example.demo.entities.Suscripcion;
import com.example.demo.enums.EstadoSuscripcion;
import com.example.demo.repositories.FacturaRepository;
import com.example.demo.repositories.SuscripcionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que automatiza la generación de facturas recurrentes
 * 
 * IMPORTANTE: Este servicio se ejecuta automáticamente usando @Scheduled
 * @Scheduled(cron = "0 0 2 * * *") -> Se ejecuta todos los días a las 2 AM
 * 
 * En producción, esto generaría facturas automáticamente cada día para
 * las suscripciones que hayan llegado a su fecha de renovación.
 */
@Service
@Transactional
public class FacturacionAutomaticaService {

    private static final Logger logger = LoggerFactory.getLogger(FacturacionAutomaticaService.class);

    private final SuscripcionRepository suscripcionRepository;
    private final FacturaRepository facturaRepository;
    private final SuscripcionService suscripcionService;

    public FacturacionAutomaticaService(
            SuscripcionRepository suscripcionRepository,
            FacturaRepository facturaRepository,
            SuscripcionService suscripcionService) {
        this.suscripcionRepository = suscripcionRepository;
        this.facturaRepository = facturaRepository;
        this.suscripcionService = suscripcionService;
    }

    /**
     * Tarea programada que se ejecuta todos los días a las 2 AM
     * Busca suscripciones activas cuya fecha de renovación haya llegado
     * y genera automáticamente una factura por el período
     * 
     * NOTA: Para testing, puedes cambiar el cron a cada minuto:
     * @Scheduled(cron = "0 * * * * *")
     */
    @Scheduled(cron = "0 0 2 * * *")  // Diariamente a las 2 AM
    public void generarFacturasRecurrentes() {
        logger.info("=== Iniciando proceso de facturación automática ===");
        
        LocalDate hoy = LocalDate.now();
        
        // Buscar todas las suscripciones activas
        List<Suscripcion> suscripcionesActivas = suscripcionRepository
                .findByEstado(EstadoSuscripcion.ACTIVA);
        
        logger.info("Suscripciones activas encontradas: {}", suscripcionesActivas.size());
        
        int facturasGeneradas = 0;
        
        for (Suscripcion suscripcion : suscripcionesActivas) {
            // Verificar si llegó la fecha de renovación
            if (!hoy.isBefore(suscripcion.getFechaRenovacion())) {
                try {
                    Optional<Factura> facturaOpt = suscripcionService
                            .generarFacturaSiCorresponde(suscripcion, hoy);
                    
                    if (facturaOpt.isPresent()) {
                        Factura factura = facturaOpt.get();
                        facturaRepository.save(factura);
                        suscripcionRepository.save(suscripcion); // Actualizar fecha de renovación
                        
                        logger.info("✓ Factura generada para suscripción ID: {} - Usuario: {} - Monto: ${}",
                                suscripcion.getId(),
                                suscripcion.getUsuario().getEmail(),
                                factura.getMonto());
                        
                        facturasGeneradas++;
                    }
                } catch (Exception e) {
                    logger.error("Error al generar factura para suscripción ID: {}", 
                            suscripcion.getId(), e);
                }
            }
        }
        
        logger.info("=== Proceso finalizado: {} facturas generadas ===", facturasGeneradas);
    }

    /**
     * Método manual para forzar la generación de facturas (útil para testing)
     * Este método puede ser llamado desde un endpoint o desde tests
     */
    public int generarFacturasManuales() {
        logger.info("=== Generación manual de facturas iniciada ===");
        generarFacturasRecurrentes();
        return (int) suscripcionRepository.findByEstado(EstadoSuscripcion.ACTIVA).size();
    }
}
