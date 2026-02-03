package com.example.demo.enums;

/**
 * Enum que representa los diferentes tipos de métodos de pago.
 * 
 * EXPLICACIÓN: Este enum se usará junto con la herencia de tablas (TABLE_PER_CLASS)
 * en las entidades de pago. Cada tipo de pago tendrá su propia tabla con campos específicos.
 */
public enum TipoPago {
    /**
     * Pago con tarjeta de crédito/débito
     */
    TARJETA("Tarjeta de Crédito/Débito", "Pago mediante tarjeta"),
    
    /**
     * Pago mediante PayPal
     */
    PAYPAL("PayPal", "Pago mediante cuenta de PayPal"),
    
    /**
     * Pago mediante transferencia bancaria
     */
    TRANSFERENCIA("Transferencia Bancaria", "Pago mediante transferencia bancaria directa");

    private final String nombre;
    private final String descripcion;

    TipoPago(String nombre, String descripcion) {
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
