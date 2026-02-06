package com.example.demo.controllers;

import com.example.demo.entities.Plan;
import com.example.demo.enums.TipoPlan;
import com.example.demo.repositories.PlanRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PlanController {

    private final PlanRepository planRepository;

    public PlanController(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("planes", planRepository.findAll());
        return "index";
    }

    @GetMapping("/planes")
    public String listar(Model model) {
        model.addAttribute("planes", planRepository.findAll());
        return "planes/lista";
    }

    // Rutas de creación de planes deshabilitadas
    // Los planes se crean automáticamente en DataInitializer
    /*
    @GetMapping("/planes/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("plan", new Plan());
        model.addAttribute("tiposPlan", TipoPlan.values());
        return "planes/nuevo";
    }

    @PostMapping("/planes")
    public String crear(@Valid @ModelAttribute("plan") Plan plan, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tiposPlan", TipoPlan.values());
            return "planes/nuevo";
        }
        planRepository.save(plan);
        return "redirect:/planes";
    }
    */
}