package com.example.demo.config;

import com.example.demo.entities.Plan;
import com.example.demo.enums.TipoPlan;
import com.example.demo.repositories.PlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Inicializador de datos para la aplicación
 * Se ejecuta al arrancar y crea los planes básicos si no existen
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(PlanRepository planRepository) {
        return args -> {
            // Verificar si ya existen planes
            if (planRepository.count() == 0) {
                System.out.println("=== Inicializando Planes ===");
                
                // Plan Basic
                Plan basic = Plan.builder()
                        .tipoPlan(TipoPlan.BASIC)
                        .nombre("Plan Basic")
                        .precioMensual(new BigDecimal("9.99"))
                        .activo(true)
                        .build();
                planRepository.save(basic);
                System.out.println("✓ Plan Basic creado: $9.99/mes");
                
                // Plan Premium
                Plan premium = Plan.builder()
                        .tipoPlan(TipoPlan.PREMIUM)
                        .nombre("Plan Premium")
                        .precioMensual(new BigDecimal("29.99"))
                        .activo(true)
                        .build();
                planRepository.save(premium);
                System.out.println("✓ Plan Premium creado: $29.99/mes");
                
                // Plan Enterprise
                Plan enterprise = Plan.builder()
                        .tipoPlan(TipoPlan.ENTERPRISE)
                        .nombre("Plan Enterprise")
                        .precioMensual(new BigDecimal("99.99"))
                        .activo(true)
                        .build();
                planRepository.save(enterprise);
                System.out.println("✓ Plan Enterprise creado: $99.99/mes");
                
                System.out.println("=== Planes inicializados correctamente ===\n");
            } else {
                System.out.println("=== Planes ya existen en la base de datos ===\n");
            }
        };
    }
}
