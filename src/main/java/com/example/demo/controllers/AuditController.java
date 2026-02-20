package com.example.demo.controllers;

import com.example.demo.entities.Suscripcion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/auditoria")
public class AuditController {

    private static final Logger log = LoggerFactory.getLogger(AuditController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public String verAuditoria(Model model) {
        try {
            AuditReader auditReader = AuditReaderFactory.get(entityManager);

            // Obtener historial de suscripciones
            @SuppressWarnings("unchecked")
            List<Object[]> revisions = (List<Object[]>) auditReader.createQuery()
                    .forRevisionsOfEntity(Suscripcion.class, false, true)
                    .addOrder(AuditEntity.revisionNumber().desc())
                    .getResultList();

            model.addAttribute("revisions", revisions);
            return "auditoria";
        } catch (Exception e) {
            log.error("Error al obtener datos de auditoría: ", e);
            model.addAttribute("error", "Error al cargar la auditoría: " + e.getMessage());
            return "index";
        }
    }
}
