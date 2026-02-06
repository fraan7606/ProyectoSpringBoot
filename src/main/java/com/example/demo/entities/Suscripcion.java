package com.example.demo.entities;

import com.example.demo.enums.EstadoSuscripcion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripciones", indexes = {
    @Index(name = "idx_suscripcion_estado", columnList = "estado"),
    @Index(name = "idx_suscripcion_renovacion", columnList = "fecha_renovacion")
})
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Plan plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSuscripcion estado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_renovacion", nullable = false)
    private LocalDate fechaRenovacion;

    @Column(name = "fecha_cancelacion")
    private LocalDate fechaCancelacion;

    @Column(name = "ultimo_cambio_plan")
    private LocalDateTime ultimoCambioPlan;

    /**
     * Método para establecer la relación bidireccional con Usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}