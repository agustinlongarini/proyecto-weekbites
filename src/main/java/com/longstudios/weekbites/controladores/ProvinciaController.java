package com.longstudios.weekbites.controladores;

import com.longstudios.weekbites.entidades.Provincia;
import com.longstudios.weekbites.servicios.ProvinciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provincias")
@RequiredArgsConstructor
public class ProvinciaController {

    private final ProvinciaService provinciaService;

    @GetMapping
    public List<Provincia> obtenerProvincias() {
        return provinciaService.obtenerProvincias();
    }

}

