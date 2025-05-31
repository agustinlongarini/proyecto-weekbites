package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.Localidad;
import com.longstudios.weekbites.servicios.LocalidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
@RequiredArgsConstructor
public class LocalidadController {

    private final LocalidadService localidadService;

    @GetMapping
    public List<Localidad> obtenerLocalidadesPorProvincia(@RequestParam("provinciaId") Long provinciaId) {
        return localidadService.obtenerLocalidadesPorProvincia(provinciaId);
    }
}
