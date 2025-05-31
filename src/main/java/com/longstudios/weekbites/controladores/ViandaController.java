package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.TipoDieta;
import com.longstudios.weekbites.entidades.Vianda;
import com.longstudios.weekbites.servicios.TipoDietaService;
import com.longstudios.weekbites.servicios.ViandaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("vianda")
public class ViandaController {

    private final ViandaService viandaService;
    private final TipoDietaService tipoDietaService;

    @GetMapping("/crear")
    public String mostrarFormularioVianda(Model model){
        List<TipoDieta> tipoDietas = tipoDietaService.obtenerTiposDietas();
        model.addAttribute("vianda", new Vianda());
        model.addAttribute("todosLosTipoDietas", tipoDietas);
        return "formVianda";
    }

    @PostMapping("/crear")
    public String procesarVianda(@ModelAttribute Vianda vianda){
        viandaService.crearVianda(vianda);
        return "redirect:/vianda/listado";
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificacion(Model model,@PathVariable Long id){
        Vianda vianda = viandaService.obtenerViandaPorId(id);
        List<TipoDieta> tipoDietas = tipoDietaService.obtenerTiposDietas();
        model.addAttribute("vianda", vianda);
        model.addAttribute("todosLosTipoDietas", tipoDietas);
        return "modificarVianda";
    }

    @PostMapping("/{id}/modificar")
    public String modificarVianda(@PathVariable Long id, @ModelAttribute Vianda vianda) {
        viandaService.modificarVianda(id, vianda);
        return "redirect:/vianda/listado";
    }

    @PostMapping("{id}/baja")
    public String darDeBajaVianda(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            viandaService.darDeBajaVianda(id);
            redirectAttributes.addFlashAttribute("success", "Vianda dada de baja correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vianda/listado";
    }

    @PostMapping("{id}/reactivar")
    public String reactivarVianda(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        viandaService.reactivarVianda(id);
        redirectAttributes.addFlashAttribute("success", "Vianda reactivada correctamente");
        return "redirect:/vianda/listado";
    }

    @GetMapping("{id}/detalle")
    public String detalleVianda(@PathVariable Long id, Model model){
        Vianda vianda = viandaService.obtenerViandaPorId(id);
        model.addAttribute("vianda", vianda);
        return "detalleVianda";
    }

    @GetMapping("/listado")
    public String listarViandas(Model model){
        List<Vianda> viandas = viandaService.obtenerViandas();
        model.addAttribute("viandas", viandas);
        return "listarViandas";
    }

}
