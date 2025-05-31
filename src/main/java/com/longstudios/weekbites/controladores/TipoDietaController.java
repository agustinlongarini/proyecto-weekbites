package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.TipoDieta;
import com.longstudios.weekbites.servicios.TipoDietaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dieta")
public class TipoDietaController {

    private final TipoDietaService tipoDietaService;

    @GetMapping("/crear")
    public String mostrarFormularioTipoDieta(Model model){
        model.addAttribute("tipoDieta", new TipoDieta());
        return "formTipoDieta";
    }

    @PostMapping("/crear")
    public String procesarTipoDieta(@ModelAttribute TipoDieta tipoDieta){
        tipoDietaService.crearTipoDieta(tipoDieta);
        return "redirect:/dieta/listado";
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificacion(@PathVariable Long id, Model model) {
        TipoDieta tipoDieta = tipoDietaService.obtenerTipoDietaPorId(id);
        model.addAttribute("tipoDieta", tipoDieta);
        return "modificarTipoDieta";
    }

    @PostMapping("/{id}/modificar")
    public String modificarTipoDieta(@PathVariable Long id, @ModelAttribute TipoDieta tipoDieta) {
        tipoDietaService.modificarTipoDieta(id, tipoDieta);
        return "redirect:/dieta/listado";
    }

    @PostMapping("{id}/baja")
    public String darDeBajaTipoDieta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tipoDietaService.darDeBajaTipoDieta(id);
            redirectAttributes.addFlashAttribute("success", "Tipo dieta dado de baja correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dieta/listado";
    }

    @PostMapping("{id}/reactivar")
    public String reactivarTipoDieta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        tipoDietaService.reactivarTipoDieta(id);
        redirectAttributes.addFlashAttribute("success", "Tipo dieta reactivado correctamente");
        return "redirect:/dieta/listado";
    }

    @GetMapping("/listado")
    public String listarTipoDietas(Model model){
        List<TipoDieta> tipoDietas = tipoDietaService.obtenerTiposDietas();
        model.addAttribute("tipoDietas", tipoDietas);
        return "listarTipoDietas";
    }

}
