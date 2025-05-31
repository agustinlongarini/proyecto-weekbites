package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.dtos.ModificarSuscripcionDTO;
import com.longstudios.weekbites.dtos.SuscripcionDTO;
import com.longstudios.weekbites.entidades.HistorialSuscripcion;
import com.longstudios.weekbites.entidades.Provincia;
import com.longstudios.weekbites.entidades.Suscripcion;
import com.longstudios.weekbites.entidades.TipoDieta;
import com.longstudios.weekbites.enums.FormaEntrega;
import com.longstudios.weekbites.servicios.ProvinciaService;
import com.longstudios.weekbites.servicios.SuscripcionService;
import com.longstudios.weekbites.servicios.TipoDietaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/suscripcion")
public class SuscripcionController {

    private final ProvinciaService provinciaService;
    private final TipoDietaService tipoDietaService;
    private final SuscripcionService suscripcionService;

    @GetMapping
    public String mostrarFormularioSuscripcion(Model model) {
        SuscripcionDTO suscripcionDTO = new SuscripcionDTO();
        List<Provincia> provincias = provinciaService.obtenerProvincias();
        List<TipoDieta> tiposDieta = tipoDietaService.obtenerTiposDietas();
        model.addAttribute("suscripcionDTO", suscripcionDTO);
        model.addAttribute("tiposDieta", tiposDieta);
        model.addAttribute("frecuencias", suscripcionService.obtenerFrecuencias());
        model.addAttribute("formasEntrega", Arrays.asList(FormaEntrega.values()));
        model.addAttribute("provincias", provincias);
        return "formSuscripcion";
    }

    @PostMapping
    public String procesarSuscripcion(@ModelAttribute @Validated SuscripcionDTO suscripcionDTO, BindingResult result, Model model) {
        if (!suscripcionDTO.getPassword().equals(suscripcionDTO.getPassword2())) {
            result.rejectValue("password2", "error.password2", "Las contraseñas no coinciden");
        }
        if (result.hasErrors()) {
            List<Provincia> provincias = provinciaService.obtenerProvincias();
            List<TipoDieta> tiposDieta = tipoDietaService.obtenerTiposDietas();
            model.addAttribute("tiposDieta", tiposDieta);
            model.addAttribute("frecuencias", suscripcionService.obtenerFrecuencias());
            model.addAttribute("formasEntrega", Arrays.asList(FormaEntrega.values()));
            model.addAttribute("provincias", provincias);
            return "formSuscripcion";  // Redirige de nuevo al formulario
        }
        Suscripcion suscripcion = suscripcionService.crearSuscripcion(suscripcionDTO);
        return "redirect:/suscripcion/confirmacion/" + suscripcion.getId();
    }

    @GetMapping("/confirmacion/{id}")
    public String mostrarConfirmacion(@PathVariable Long id, Model model) {
        Suscripcion suscripcion = suscripcionService.obtenerSuscripcionPorId(id);
        model.addAttribute("suscripcion", suscripcion);
        return "confirmacion";
    }

    @PreAuthorize("hasAuthority('MODIFICAR_SUSCRIPCION')")
    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificacion(@PathVariable Long id, Model model) {
        Suscripcion suscripcion = suscripcionService.obtenerSuscripcionPorId(id);
        model.addAttribute("suscripcion", suscripcion);
        model.addAttribute("tiposDieta", tipoDietaService.obtenerTiposDietas());
        model.addAttribute("frecuencias", suscripcionService.obtenerFrecuencias());
        model.addAttribute("formasEntrega", Arrays.asList(FormaEntrega.values()));
        model.addAttribute("provincias", provinciaService.obtenerProvincias());
        return "modificarSuscripcion";
    }

    @PreAuthorize("hasAuthority('MODIFICAR_SUSCRIPCION')")
    @PostMapping("/{id}/modificar")
    public String modificarSuscripcion(@PathVariable Long id, @ModelAttribute("suscripcion") ModificarSuscripcionDTO suscripcion) {
        suscripcionService.modificarSuscripcion(id, suscripcion);
        return "redirect:/cliente";
    }

    @PreAuthorize("hasAuthority('SUSPENDER_SUSCRIPCION')")
    @PostMapping("/{id}/suspender")
    public String suspenderSuscripcion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            suscripcionService.suspenderSuscripcion(id);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cliente";
        }
        return "redirect:/cliente";
    }

    @PreAuthorize("hasAuthority('REACTIVAR_SUSCRIPCION')")
    @PostMapping("/{id}/reactivar")
    public String reactivarSuscripcion(@PathVariable Long id) {
        suscripcionService.reactivarSuscripcion(id);
        return "redirect:/cliente";
    }

    @PreAuthorize("hasAuthority('HISTORIAL_SUSCRIPCION')")
    @GetMapping("/{id}/historial")
    public String consultarHistorial(@PathVariable Long id, Model model) {
        List<HistorialSuscripcion> historial = suscripcionService.consultarHistorialSuscripciones(id);
        model.addAttribute("historial", historial);
        return "historial";
    }

}
