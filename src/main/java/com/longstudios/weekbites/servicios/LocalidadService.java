package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.Localidad;
import com.longstudios.weekbites.entidades.Provincia;
import com.longstudios.weekbites.repositorios.LocalidadRepository;
import com.longstudios.weekbites.repositorios.ProvinciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalidadService {

    private final RestTemplate restTemplate;
    private final ProvinciaRepository provinciaRepository;
    private final LocalidadRepository localidadRepository;
    private final String URL_LOCALIDADES = "https://apis.datos.gob.ar/georef/api/localidades?provincia={provincia}&max=5000";

    public List<Localidad> obtenerLocalidadesPorProvincia(Long provinciaId) {
        Provincia provincia = provinciaRepository.findById(provinciaId).orElseThrow();
        String url = "https://apis.datos.gob.ar/georef/api/localidades?provincia=" + provincia.getNombreProvincia() + "&max=5000";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        List<Map<String, Object>> lista = (List<Map<String, Object>>) response.getBody().get("localidades");
        List<Localidad> localidades = lista.stream()
                .map(l -> {
                    Long id = Long.valueOf((String) l.get("id"));
                    String nombre = (String) l.get("nombre");
                    return new Localidad(id, nombre, provincia);
                })
                .collect(Collectors.toList());
        localidadRepository.saveAll(localidades);
        return localidades;
    }

    public Localidad obtenerLocalidadPorId(Long provinciaId, Long localidadId) {
        List<Localidad> localidades = obtenerLocalidadesPorProvincia(provinciaId);
        return localidades.stream()
                .filter(localidad -> localidad.getId().equals(localidadId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Localidad no encontrada con ID: " + localidadId));
    }
}


