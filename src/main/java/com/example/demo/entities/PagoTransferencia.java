package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "pagos_transferencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PagoTransferencia extends Pago {

    @Column(name = "banco", length = 80)
    private String banco;

    @Column(name = "referencia", length = 80)
    private String referencia;
}