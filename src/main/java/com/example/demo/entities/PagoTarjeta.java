package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "pagos_tarjeta")
@Audited
@Data
@EqualsAndHashCode(callSuper = true)
public class PagoTarjeta extends MetodoPago {
    private String numeroTarjeta;
    private String fechaCaducidad;
}
