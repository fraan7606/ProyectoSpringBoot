package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "pagos_tarjeta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PagoTarjeta extends Pago {

    @Column(name = "titular", length = 100)
    private String titular;

    @Column(name = "ultimos4", length = 4)
    private String ultimos4;

    @Column(name = "vencimiento", length = 7)
    private String vencimiento;
}