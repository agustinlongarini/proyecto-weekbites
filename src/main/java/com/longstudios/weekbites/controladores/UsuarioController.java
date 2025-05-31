package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.Rol;
import com.longstudios.weekbites.entidades.Usuario;
import com.longstudios.weekbites.servicios.RolService;
import com.longstudios.weekbites.servicios.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;


    @GetMapping("/crear")
    public String mostrarFormularioUsuario(Model model){
        List<Rol> roles = rolService.obtenerRoles();
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("todosLosRoles", roles);
        return "formUsuario";
    }

    @PostMapping("/crear")
    public String procesarUsuario(@ModelAttribute Usuario usuario){
        usuarioService.crearUsuario(usuario);
        return "redirect:/usuario/listado";
    }

    @GetMapping("/{id}/modificar")
    public String mostrarFormularioModificacion(Model model,@PathVariable Long id){
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        List<Rol> roles = rolService.obtenerRoles();
        model.addAttribute("usuario", usuario);
        model.addAttribute("todosLosRoles", roles);
        return "modificarUsuario";
    }

    @PostMapping("/{id}/modificar")
    public String modificarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        usuarioService.modificarUsuario(id, usuario);
        return "redirect:/usuario/listado";
    }

    @PostMapping("/{id}/baja")
    public String darDeBajaUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.darDeBajaUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario dado de baja correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/listado";
    }

    @PostMapping("{id}/reactivar")
    public String reactivarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.reactivarUsuario(id);
        redirectAttributes.addFlashAttribute("success", "Usuario reactivado correctamente");
        return "redirect:/usuario/listado";
    }

    @GetMapping("/{id}/contrasenia")
    public String mostrarFormularioContrasenia(@PathVariable Long id, Model model){
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "modificarContrasenia";
    }

    @PostMapping("/{id}/contrasenia")
    public String cambiarContrasenia(@RequestParam String passwordActual,
                                  @RequestParam String nuevaPassword,
                                  @RequestParam String confirmarPassword,
                                  @PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {

        if (!nuevaPassword.equals(confirmarPassword)) {
            redirectAttributes.addFlashAttribute("error", "La nueva contraseña no coincide con la confirmación.");
            return "redirect:/usuario/" + id + "/contrasenia";
        }

        try {
            usuarioService.cambiarContrasenia(id, passwordActual, nuevaPassword);
            redirectAttributes.addFlashAttribute("success", "Contraseña actualizada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuario/listado";
    }

    @GetMapping("/listado")
    public String listarUsuarios(Model model){
        List<Usuario> usuarios = usuarioService.obtenerUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "listarUsuarios";
    }

}
