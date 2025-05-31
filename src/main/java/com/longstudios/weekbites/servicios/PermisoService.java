package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.Permiso;
import com.longstudios.weekbites.repositorios.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermisoService {

    private final PermisoRepository permisoRepository;

    public List<Permiso> obtenerPermisos(){
        return permisoRepository.findAll();
    }

}
