package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.envers.Audited;

/**
 * Entidad Perfil - Información adicional del usuario
 * 
 * EXPLICACIÓN:
 * Esta entidad está separada de Usuario para mantener el principio de
 * Single Responsibility (Responsabilidad Única). Usuario maneja la autenticación,
 * mientras que Perfil maneja la información personal.
 * 
 * @Audited: También auditamos el perfil para saber cuándo el usuario
 *           actualiza su información personal
 */
@Entity
@Table(name = "perfiles")
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación One-to-One con Usuario
     * 
     * @JoinColumn: Define la columna de FK (Foreign Key) en esta tabla
     *   - name: nombre de la columna FK en la tabla perfiles
     *   - nullable: el perfil DEBE pertenecer a un usuario
     *   - unique: cada perfil pertenece a un solo usuario
     * 
     * Esta es el lado "dueño" de la relación (tiene la FK)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String empresa;

    /**
     * Dirección del usuario
     */
    @Column(length = 200)
    private String direccion;

    @Column(length = 50)
    private String ciudad;

    @Column(length = 50)
    private String pais;

    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    /**
     * Método para establecer la relación bidireccional con Usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Método toString personalizado que excluye usuario para evitar
     * LazyInitializationException y recursión infinita
     */
    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", telefono='" + telefono + '\'' +
                ", empresa='" + empresa + '\'' +
                '}';
    }
}
