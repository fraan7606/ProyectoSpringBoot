package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "pagos_transferencia")
@Audited
@Data
@EqualsAndHashCode(callSuper = true)
public class PagoTransferencia extends MetodoPago {
    private String iban;
    private String banco;
}
