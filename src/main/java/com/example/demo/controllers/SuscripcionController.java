package com.example.demo.controllers;

import com.example.demo.audit.AuditContext;
import com.example.demo.entities.Factura;
import com.example.demo.entities.Plan;
import com.example.demo.entities.Suscripcion;
import com.example.demo.repositories.FacturaRepository;
import com.example.demo.repositories.PlanRepository;
import com.example.demo.repositories.SuscripcionRepository;
import com.example.demo.services.SuscripcionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/suscripciones")
public class SuscripcionController {

    private final SuscripcionRepository suscripcionRepository;
    private final PlanRepository planRepository;
    private final FacturaRepository facturaRepository;
    private final SuscripcionService suscripcionService;

    public SuscripcionController(
            SuscripcionRepository suscripcionRepository,
            PlanRepository planRepository,
            FacturaRepository facturaRepository,
            SuscripcionService suscripcionService) {
        this.suscripcionRepository = suscripcionRepository;
        this.planRepository = planRepository;
        this.facturaRepository = facturaRepository;
        this.suscripcionService = suscripcionService;
    }

    @GetMapping("/{id}")
    public String verSuscripcion(@PathVariable Long id, Model model) {
        Optional<Suscripcion> suscripcionOpt = suscripcionRepository.findById(id);
        if (suscripcionOpt.isEmpty()) {
            return "redirect:/";
        }

        Suscripcion suscripcion = suscripcionOpt.get();
        model.addAttribute("suscripcion", suscripcion);
        model.addAttribute("usuario", suscripcion.getUsuario());
        model.addAttribute("facturas", facturaRepository.findBySuscripcionOrderByFechaEmisionDesc(suscripcion));
        
        return "suscripciones/detalle";
    }

    @GetMapping("/{id}/cambiar-plan")
    public String mostrarCambioPlan(@PathVariable Long id, Model model) {
        Optional<Suscripcion> suscripcionOpt = suscripcionRepository.findById(id);
        if (suscripcionOpt.isEmpty()) {
            return "redirect:/";
        }

        Suscripcion suscripcion = suscripcionOpt.get();
        model.addAttribute("suscripcion", suscripcion);
        model.addAttribute("planActual", suscripcion.getPlan());
        model.addAttribute("planesDisponibles", planRepository.findAll());
        
        return "suscripciones/cambiar-plan";
    }

    @PostMapping("/{id}/cambiar-plan")
    public String cambiarPlan(
            @PathVariable Long id,
            @RequestParam("nuevoPlanId") Long nuevoPlanId,
            @RequestParam(value = "actor", defaultValue = "usuario@sistema.com") String actor,
            RedirectAttributes redirectAttributes) {

        Optional<Suscripcion> suscripcionOpt = suscripcionRepository.findById(id);
        Optional<Plan> nuevoPlanOpt = planRepository.findById(nuevoPlanId);

        if (suscripcionOpt.isEmpty() || nuevoPlanOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Suscripción o plan no encontrado");
            return "redirect:/";
        }

        Suscripcion suscripcion = suscripcionOpt.get();
        Plan nuevoPlan = nuevoPlanOpt.get();
        Plan planAnterior = suscripcion.getPlan();

        // Verificar que no sea el mismo plan
        if (planAnterior.getId().equals(nuevoPlan.getId())) {
            redirectAttributes.addFlashAttribute("warning", "Ya tienes activo el plan " + nuevoPlan.getNombre());
            return "redirect:/usuarios/" + suscripcion.getUsuario().getId();
        }

        // Realizar el cambio de plan con prorrateo
        Optional<Factura> facturaProrrateo = suscripcionService.cambiarPlanConProrrateo(
                suscripcion, 
                nuevoPlan, 
                LocalDate.now(), 
                actor
        );

        // Guardar los cambios
        suscripcionRepository.save(suscripcion);
        
        // Guardar la factura si se generó
        if (facturaProrrateo.isPresent()) {
            Factura factura = facturaProrrateo.get();
            facturaRepository.save(factura);
            
            redirectAttributes.addFlashAttribute("success", 
                String.format("Plan cambiado de %s a %s. Se generó una factura de prorrateo por $%.2f", 
                    planAnterior.getNombre(), 
                    nuevoPlan.getNombre(), 
                    factura.getMonto()));
        } else {
            redirectAttributes.addFlashAttribute("success", 
                String.format("Plan cambiado de %s a %s. No se generó cargo adicional (el nuevo plan es más barato o igual).", 
                    planAnterior.getNombre(), 
                    nuevoPlan.getNombre()));
        }

        return "redirect:/usuarios/" + suscripcion.getUsuario().getId();
    }
}
