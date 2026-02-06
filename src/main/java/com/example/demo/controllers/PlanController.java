package com.example.demo.controllers;

import com.example.demo.repositories.PlanRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}