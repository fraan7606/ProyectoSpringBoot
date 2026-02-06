package com.example.demo.enums;

/**
 * Enum que representa los diferentes estados de una suscripción.
 * 
 * EXPLICACIÓN: Este enum es fundamental para el ciclo de vida de las suscripciones.
 * Hibernate Envers (@Audited) guardará cada cambio de estado, permitiéndonos
 * saber cuándo y por qué una suscripción cambió de ACTIVA a CANCELADA, por ejemplo.
 */
public enum EstadoSuscripcion {
    /**
     * Suscripción activa y al día con los pagos
     */
    ACTIVA("Activa", "La suscripción está activa y funcionando"),
    
    /**
     * Suscripción cancelada por el usuario
     */
    CANCELADA("Cancelada", "El usuario canceló su suscripción"),
    
    /**
     * Suscripción con pagos pendientes o rechazados
     */
    MOROSA("Morosa", "Existen pagos pendientes");

    private final String nombre;
    private final String descripcion;

    EstadoSuscripcion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método útil para saber si la suscripción permite acceso a la plataforma
     */
    public boolean permiteAcceso() {
        return this == ACTIVA;
    }
}
