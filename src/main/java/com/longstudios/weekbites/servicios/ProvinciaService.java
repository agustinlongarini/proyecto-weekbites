package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.Provincia;
import com.longstudios.weekbites.repositorios.ProvinciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProvinciaService {

    private final RestTemplate restTemplate;
    private final ProvinciaRepository provinciaRepository;
    private final String URL_PROVINCIAS = "https://apis.datos.gob.ar/georef/api/provincias";

    public List<Provincia> obtenerProvincias() {
        if (provinciaRepository.count() == 0) {
            ResponseEntity<Map> response = restTemplate.getForEntity("https://apis.datos.gob.ar/georef/api/provincias", Map.class);
            List<Map<String, Object>> lista = (List<Map<String, Object>>) response.getBody().get("provincias");
            List<Provincia> provincias = lista.stream()
                    .map(p -> new Provincia(Long.valueOf((String) p.get("id")), (String) p.get("nombre")))
                    .collect(Collectors.toList());
            provinciaRepository.saveAll(provincias);
        }
        return provinciaRepository.findAll();
    }
}

