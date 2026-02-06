package com.example.demo.entities;

import com.example.demo.enums.TipoPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Entity
@Table(name = "planes", indexes = {
    @Index(name = "idx_plan_activo", columnList = "activo")
})
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_plan", nullable = false, unique = true, length = 20)
    @NotNull
    private TipoPlan tipoPlan;

    @Column(nullable = false, length = 80)
    @NotBlank
    private String nombre;

    @Column(name = "precio_mensual", nullable = false, precision = 12, scale = 2)
    @NotNull
    @Positive
    private BigDecimal precioMensual;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}