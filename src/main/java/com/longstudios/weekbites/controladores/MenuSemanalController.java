package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.MenuSemanal;
import com.longstudios.weekbites.servicios.MenuSemanalService;
import com.longstudios.weekbites.servicios.TipoDietaService;
import com.longstudios.weekbites.servicios.ViandaService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuSemanalController {

    private final TipoDietaService tipoDietaService;
    private final ViandaService viandaService;
    private final MenuSemanalService menuSemanalService;

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("tiposDieta", tipoDietaService.obtenerTiposDietas());
        model.addAttribute("viandas", viandaService.obtenerViandas());
        model.addAttribute("menu", new MenuSemanal());
        return "formMenu";
    }

    @PostMapping("/crear")
    public String procesarMenu(
            @RequestParam("semana") int semana,
            @RequestParam("tipoDietaId") Long tipoDietaId,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Map<String, String[]> parametros = request.getParameterMap();
            menuSemanalService.crearMenu(semana, tipoDietaId, parametros);
            return "redirect:/menus/listado";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menus/crear";
        }
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        MenuSemanal menu = menuSemanalService.obtenerMenuSemanalPorId(id);
        model.addAttribute("viandas", viandaService.obtenerViandasPorTipoDietaActivas(menu.getId()));
        model.addAttribute("menu", menu);
        return "modificarMenu";
    }

    @PostMapping("/{id}/modificar")
    public String procesarEdicion(
            @PathVariable Long id,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Map<String, String[]> parametros = request.getParameterMap();
            menuSemanalService.modificarMenu(id, parametros);
            redirectAttributes.addFlashAttribute("success", "Menú modificado correctamente.");
            return "redirect:/menus/listado";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menus/" + id + "/modificar";
        }
    }

    @PostMapping("/{id}/baja")
    public String darDeBajaMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        menuSemanalService.darDeBajaMenu(id);
        redirectAttributes.addFlashAttribute("success", "Menú dado de baja correctamente.");
        return "redirect:/menus/listado";
    }

    @PostMapping("/{id}/reactivar")
    public String reactivarMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        menuSemanalService.reactivarMenu(id);
        redirectAttributes.addFlashAttribute("success", "Menú reactivado correctamente.");
        return "redirect:/menus/listado";
    }

    @GetMapping("/listado")
    public String verListadoMenus(Model model) {
        model.addAttribute("menus", menuSemanalService.obtenerMenuSemanales());
        return "listarMenus";
    }

    @GetMapping("/{id}/detalle")
    public String verMenuSemanal(@PathVariable Long id, Model model) {
        MenuSemanal menu = menuSemanalService.obtenerMenuSemanalPorId(id);
        model.addAttribute("menu", menu);
        return "detalleMenu";
    }

}