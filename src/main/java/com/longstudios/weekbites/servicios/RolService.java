package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.Rol;
import com.longstudios.weekbites.entidades.Usuario;
import com.longstudios.weekbites.repositorios.RolRepository;
import com.longstudios.weekbites.repositorios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public void crearRol(Rol rol){
        rolRepository.save(rol);
    }

    public void modificarRol(Long id, Rol rolModificado) {
        Rol rolExistente = rolRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        rolExistente.setNombre(rolModificado.getNombre());
        rolExistente.setPermisos(rolModificado.getPermisos());
        rolRepository.save(rolExistente);
    }

    public void darDeBajaRol(Long id) {
        Rol rolExistente = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        if (usuarioRepository.existsByRolAndFechaBajaIsNull(rolExistente)) {
            throw new IllegalArgumentException("No se puede dar de baja un rol asociado a un usuario activo.");
        }

        rolExistente.setFechaBaja(LocalDate.now());
        rolRepository.save(rolExistente);
    }

    public void reactivarRol(Long id) {
        Rol rolExistente = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        rolExistente.setFechaBaja(null);
        rolRepository.save(rolExistente);
    }

    public List<Rol> obtenerRoles(){
        return rolRepository.findAll();
    }

    public Rol buscarRolPorId(Long id){
        return rolRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
    }

}
