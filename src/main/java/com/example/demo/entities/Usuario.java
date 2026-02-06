package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

/**
 * Entidad Usuario - Representa a los usuarios de la plataforma SaaS
 * 
 * EXPLICACIÓN DE ANOTACIONES:
 * 
 * @Entity: Marca esta clase como una entidad JPA (se mapeará a una tabla en la BD)
 * @Table: Define el nombre de la tabla y restricciones adicionales
 * @Audited: De Hibernate Envers - Crea automáticamente una tabla USUARIO_AUD que
 *           guarda cada cambio realizado en esta entidad (quién, cuándo, qué cambió)
 * 
 * LOMBOK (reduce código boilerplate):
 * @Data: Genera getters, setters, toString, equals y hashCode
 * @NoArgsConstructor: Constructor sin argumentos (requerido por JPA)
 * @AllArgsConstructor: Constructor con todos los argumentos
 * @Builder: Patrón Builder para crear objetos de forma elegante
 */
@Entity
@Table(name = "usuarios")
@Audited  // ← IMPORTANTE: Esta anotación crea el historial automático
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    /**
     * @Id: Marca este campo como la clave primaria
     * @GeneratedValue: El valor se genera automáticamente
     * IDENTITY: La base de datos genera el ID (auto-increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @NotBlank: Validación - el campo no puede estar vacío
     * @Email: Validación - debe ser un email válido
     * @Column: Define características de la columna en la BD
     *   - unique: No puede haber dos usuarios con el mismo email
     *   - nullable: No puede ser null
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    /**
     * @Size: Validación - la contraseña debe tener entre 8 y 255 caracteres
     * En producción, este campo debe estar encriptado (BCrypt, etc.)
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;

    /**
     * Fecha de registro del usuario
     * @Column(updatable = false): Este campo nunca se puede modificar después de crearse
     */
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Indica si el usuario está activo en la plataforma
     */
    @Column(nullable = false)
    @Builder.Default  // Valor por defecto cuando usamos el Builder
    private Boolean activo = true;

    /**
     * Relación One-to-One con Perfil
     * 
     * EXPLICACIÓN:
     * - mappedBy: Indica que el lado "dueño" de la relación está en Perfil
     * - cascade: Las operaciones en Usuario se propagan a Perfil
     *   CascadeType.ALL: Guardar, actualizar, eliminar se propagan
     * - orphanRemoval: Si se elimina la referencia a Perfil, también se elimina de la BD
     * - fetch: LAZY = El perfil se carga solo cuando se accede (mejor performance)
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Perfil perfil;

    /**
     * Relación One-to-One con Suscripcion
     * Similar a la relación con Perfil
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Suscripcion suscripcion;

    /**
     * @PrePersist: Método que se ejecuta ANTES de guardar la entidad por primera vez
     * Útil para inicializar valores automáticamente
     */
    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }

    /**
     * Método helper para establecer la relación bidireccional con Perfil
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        if (perfil != null) {
            perfil.setUsuario(this);
        }
    }

    /**
     * Método helper para establecer la relación bidireccional con Suscripcion
     */
    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
        if (suscripcion != null) {
            suscripcion.setUsuario(this);
        }
    }
}
