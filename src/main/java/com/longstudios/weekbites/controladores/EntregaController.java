package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.servicios.EntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("entrega")
public class EntregaController {

    private final EntregaService entregaService;

    @GetMapping("/generar")
    public String mostrarFormularioGenerar(Model model) {
        model.addAttribute("anioActual", LocalDate.now().getYear());
        return "generarEntregas";
    }

    @PostMapping("/generar")
    public String generarEntregas(
            @RequestParam("anio") int anio,
            @RequestParam("semana") int semana,
            RedirectAttributes redirectAttributes) {

        try {
            entregaService.generarEntregasParaSemana(anio, semana);
            redirectAttributes.addFlashAttribute("success",
                    "Entregas generadas para la semana " + semana + " del año " + anio);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/entrega/listado";
    }

    @GetMapping("/listado")
    public String listarEntregas(Model model) {
        model.addAttribute("entregas", entregaService.obtenerTodas());
        return "listarEntregas";
    }

}
