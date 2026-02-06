package com.example.demo.repositories;

import com.example.demo.entities.Factura;
import com.example.demo.entities.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findBySuscripcionOrderByFechaEmisionDesc(Suscripcion suscripcion);
}