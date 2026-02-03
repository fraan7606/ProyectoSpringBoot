package com.example.demo.enums;

/**
 * Enum que define los diferentes tipos de planes disponibles en la plataforma SaaS.
 * 
 * EXPLICACIÓN: Los enums en Java son clases especiales que representan un grupo
 * de constantes. Son perfectos para valores que no cambiarán (como tipos de planes).
 */
public enum TipoPlan {
    BASIC("Plan Básico", "Ideal para empezar"),
    PREMIUM("Plan Premium", "Para usuarios avanzados"),
    ENTERPRISE("Plan Enterprise", "Solución empresarial completa");

    private final String nombre;
    private final String descripcion;

    /**
     * Constructor privado del enum.
     * Los enums siempre tienen constructores privados.
     */
    TipoPlan(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
