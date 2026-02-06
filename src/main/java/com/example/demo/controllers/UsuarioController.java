package com.example.demo.controllers;

import com.example.demo.entities.Perfil;
import com.example.demo.entities.Plan;
import com.example.demo.entities.Suscripcion;
import com.example.demo.entities.Usuario;
import com.example.demo.enums.EstadoSuscripcion;
import com.example.demo.repositories.PlanRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PlanRepository planRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, PlanRepository planRepository) {
        this.usuarioRepository = usuarioRepository;
        this.planRepository = planRepository;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("perfil", new Perfil());
        model.addAttribute("planes", planRepository.findAll());
        return "usuarios/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("perfil.nombre") String nombre,
            @RequestParam("perfil.apellido") String apellido,
            @RequestParam(value = "perfil.telefono", required = false) String telefono,
            @RequestParam(value = "perfil.empresa", required = false) String empresa,
            @RequestParam(value = "perfil.direccion", required = false) String direccion,
            @RequestParam(value = "perfil.ciudad", required = false) String ciudad,
            @RequestParam(value = "perfil.pais", required = false) String pais,
            @RequestParam(value = "perfil.codigoPostal", required = false) String codigoPostal,
            @RequestParam("planId") Long planId,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Validaciones básicas
        if (email == null || email.isBlank() || password == null || password.length() < 8) {
            model.addAttribute("error", "Email y contraseña (mínimo 8 caracteres) son obligatorios");
            model.addAttribute("planes", planRepository.findAll());
            return "usuarios/registro";
        }

        if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank()) {
            model.addAttribute("error", "Nombre y apellido son obligatorios");
            model.addAttribute("planes", planRepository.findAll());
            return "usuarios/registro";
        }

        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "El email ya está registrado");
            model.addAttribute("planes", planRepository.findAll());
            return "usuarios/registro";
        }

        // Buscar el plan seleccionado
        Optional<Plan> planOpt = planRepository.findById(planId);
        if (planOpt.isEmpty()) {
            model.addAttribute("error", "Plan no válido");
            model.addAttribute("planes", planRepository.findAll());
            return "usuarios/registro";
        }

        Plan plan = planOpt.get();

        // Crear usuario
        Usuario usuario = Usuario.builder()
                .email(email)
                .password(password)
                .activo(true)
                .build();

        // Crear perfil
        Perfil perfil = Perfil.builder()
                .nombre(nombre)
                .apellido(apellido)
                .telefono(telefono)
                .empresa(empresa)
                .direccion(direccion)
                .ciudad(ciudad)
                .pais(pais)
                .codigoPostal(codigoPostal)
                .usuario(usuario)
                .build();

        usuario.setPerfil(perfil);

        // Crear la suscripción
        Suscripcion suscripcion = Suscripcion.builder()
                .usuario(usuario)
                .plan(plan)
                .estado(EstadoSuscripcion.ACTIVA)
                .fechaInicio(LocalDate.now())
                .fechaRenovacion(LocalDate.now().plusDays(30))
                .build();
        
        usuario.setSuscripcion(suscripcion);

        // Guardar todo (cascade se encarga del resto)
        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("success", 
            "¡Registro exitoso! Bienvenido " + perfil.getNombre() + ". Tu suscripción al plan " + plan.getNombre() + " está activa.");
        
        return "redirect:/usuarios/" + usuario.getId();
    }

    @GetMapping("/{id}")
    public String verPerfil(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/";
        }

        Usuario usuario = usuarioOpt.get();
        model.addAttribute("usuario", usuario);
        model.addAttribute("perfil", usuario.getPerfil());
        model.addAttribute("suscripcion", usuario.getSuscripcion());
        model.addAttribute("planesDisponibles", planRepository.findAll());
        
        return "usuarios/perfil";
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/lista";
    }
}
