# SaaS System - Core Platform

Este proyecto es el núcleo de una plataforma SaaS desarrollada con **Spring Boot 3.4**, enfocada en la gestión de suscripciones, facturación automática y auditoría detallada.

## Características Principales

- **Gestión de Planes**: Soporte para planes Basic, Premium y Enterprise.
- **Suscripciones**: Ciclo de vida completo (Activa, Cancelada, Morosa).
- **Facturación Automática**: Generación de facturas cada 30 días mediante tareas programadas (@Scheduled).
- **Prorrateo Inteligente**: Cálculo automático de diferencias al subir de plan.
- **Impuestos Dinámicos**: Cálculo automático de impuestos (IVA) basado en el país del usuario (ej. España 21%, México 16%, USA 0%).
- **Auditoría Avanzada**: Registro histórico de cambios usando Hibernate Envers (quién, qué y cuándo).
- **Interfaz Web**: Panel administrativo y de usuario construido con Thymeleaf y Bootstrap.

## Requisitos Técnicos

- **Java**: 17 o superior.
- **Base de Datos**: PostgreSQL 14+.
- **Gestor de Dependencias**: Maven.

## Configuración e Instalación

1. **Clonar el repositorio**.
2. **Configurar la Base de Datos**:
   - Crear una base de datos llamada `saasdb` en PostgreSQL.
   - Ajustar las credenciales en `src/main/resources/application.properties`.
3. **Ejecutar la aplicación**:
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Acceso**:
   - Web: `http://localhost:8080`
   - Auditoría: `http://localhost:8080/admin/auditoria`

## Arquitectura del Sistema

### Diagrama Entidad-Relación (E-R)

```mermaid
erDiagram
    USUARIO ||--|| PERFIL : tiene
    USUARIO ||--o{ SUSCRIPCION : posee
    USUARIO ||--o| METODO_PAGO : configura
    SUSCRIPCION ||--o{ FACTURA : genera
    PLAN ||--o{ SUSCRIPCION : asignado_a
    METODO_PAGO <|-- PagoTarjeta : hereda
    METODO_PAGO <|-- PagoPayPal : hereda
    METODO_PAGO <|-- PagoTransferencia : hereda

    USUARIO {
        Long id
        String email
        String password
    }
    PLAN {
        Long id
        String nombre
        BigDecimal precio_mensual
        Enum tipo_plan
    }
    SUSCRIPCION {
        Long id
        Enum estado
        LocalDate fecha_inicio
        LocalDate fecha_renovacion
    }
    FACTURA {
        Long id
        BigDecimal monto_subtotal
        BigDecimal monto_impuestos
        BigDecimal monto_total
        Enum tipo_factura
    }
    METODO_PAGO {
        Long id
        String titular
    }
```

## Pruebas Unitarias y Validación

El proyecto incluye una suite de pruebas para los componentes críticos. A continuación se detalla la matriz de pruebas realizadas:

### Matriz de Casos de Prueba

| Caso de Prueba | Entrada (Input) | Resultado Esperado | Resultado Obtenido | Corrección Realizada |
| :--- | :--- | :--- | :--- | :--- |
| **Cálculo de Prorrateo Upgrade** | Plan $10 -> $30 (día 15/30) | Cobro de $10.00 subtotal | ✅ Correcto | Ninguna (inicialmente OK) |
| **Cálculo de Impuestos IVA** | Subtotal $10.00 | Total $12.10 (IVA 21%) | ✅ Correcto | Ajuste en `FacturaService` para precisión decimal |
| **Upgrade con IVA** | Diferencia $10.00 | Total $12.10 | ✅ Correcto | Se corrigió el test unitario para esperar el total con IVA |
| **Facturación Recurrente** | Plan $29.99 | Factura $36.29 (total) | ✅ Correcto | Ninguna |
| **Downgrade de Plan** | Plan $30 -> $10 | Cobro $0.00 (prorrateo) | ✅ Correcto | Ninguna |

### Ejecución de Pruebas
Ejecutar el comando para validar todos los casos:
```bash
mvn test
```

---
Desarrollado para la entrega final del proyecto SaaS.

## Referencias Útiles

* [Documentación oficial de Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
* [Guía de Spring Web](https://spring.io/guides/gs/serving-web-content/)
* [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
* [Hibernate Envers Guide](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#envers)
