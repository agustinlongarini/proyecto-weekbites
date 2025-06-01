package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.Permiso;
import com.longstudios.weekbites.entidades.Rol;
import com.longstudios.weekbites.servicios.PermisoService;
import com.longstudios.weekbites.servicios.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("rol")
public class RolController {

    private final RolService rolService;
    private final PermisoService permisoService;

    @GetMapping("/crear")
    public String mostrarFormularioRol(Model model){
        List<Permiso> permisos = permisoService.obtenerPermisos();
        model.addAttribute("rol", new Rol());
        model.addAttribute("todosLosPermisos", permisos);
        return "formRol";
    }

    @PostMapping("/crear")
    public String procesarRol(@ModelAttribute Rol rol){
        rolService.crearRol(rol);
        return "redirect:/rol/listado";
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificacion(Model model,@PathVariable Long id){
        Rol rol = rolService.buscarRolPorId(id);
        List<Permiso> permisos = permisoService.obtenerPermisos();
        model.addAttribute("rol", rol);
        model.addAttribute("todosLosPermisos", permisos);
        return "modificarRol";
    }

    @PostMapping("/{id}/modificar")
    public String modificarRol(@PathVariable Long id, @ModelAttribute Rol rol) {
        rolService.modificarRol(id, rol);
        return "redirect:/rol/listado";
    }

    @PostMapping("{id}/baja")
    public String darDeBajaRol(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rolService.darDeBajaRol(id);
            redirectAttributes.addFlashAttribute("success", "Rol dado de baja correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rol/listado";
    }

    @PostMapping("{id}/reactivar")
    public String reactivarRol(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        rolService.reactivarRol(id);
        redirectAttributes.addFlashAttribute("success", "Rol reactivado correctamente");
        return "redirect:/rol/listado";
    }

    @GetMapping("/listado")
    public String listarRoles(Model model){
        List<Rol> roles = rolService.obtenerRoles();
        model.addAttribute("roles", roles);
        return "listarRoles";
    }

    @GetMapping("/{id}/permisos")
    public String visualizarRol(@PathVariable Long id, Model model){
        Rol rol = rolService.buscarRolPorId(id);
        model.addAttribute("rol", rol);
        return "detalleRol";
    }
}
