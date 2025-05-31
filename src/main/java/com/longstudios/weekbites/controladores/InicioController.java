package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.Cliente;
import com.longstudios.weekbites.entidades.Usuario;
import com.longstudios.weekbites.servicios.SuscripcionService;
import com.longstudios.weekbites.servicios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class InicioController {

    private final UsuarioService usuarioService;
    private final SuscripcionService suscripcionService;

    @GetMapping("/")
    public String mostrarInicio() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null) {
            if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin";
            } else if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_CLIENTE"))) {
                return "redirect:/cliente";
            }
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario admin = usuarioService.buscarUsuarioPorEmail(email);
        model.addAttribute("admin", admin);
        return "admin";
    }

    @GetMapping("/cliente")
    public String clientePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(email);
        Cliente cliente = usuario.getCliente();
        model.addAttribute("cliente", cliente);
        model.addAttribute("frecuencias", suscripcionService.obtenerFrecuencias());
        return "cliente";
    }

}
