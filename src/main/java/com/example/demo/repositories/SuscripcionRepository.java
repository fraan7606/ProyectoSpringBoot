package com.example.demo.repositories;

import com.example.demo.entities.Suscripcion;
import com.example.demo.enums.EstadoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    List<Suscripcion> findByEstado(EstadoSuscripcion estado);
}