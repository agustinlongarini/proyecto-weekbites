package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.TipoDieta;
import com.longstudios.weekbites.repositorios.TipoDietaRepository;
import com.longstudios.weekbites.repositorios.ViandaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoDietaService {

    private final TipoDietaRepository tipoDietaRepository;
    private final ViandaRepository viandaRepository;

    public void crearTipoDieta(TipoDieta tipoDieta) {
        tipoDietaRepository.save(tipoDieta);
    }

    public void modificarTipoDieta(Long id, TipoDieta tipoDietaModificada){
        TipoDieta tipoDietaExistente = tipoDietaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo dieta no encontrado"));
        tipoDietaExistente.setNombreTD(tipoDietaModificada.getNombreTD());
        tipoDietaExistente.setDescripcionTD(tipoDietaModificada.getDescripcionTD());
        tipoDietaRepository.save(tipoDietaExistente);
    }

    public void darDeBajaTipoDieta(Long id){
        TipoDieta tipoDietaExistente = tipoDietaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo dieta no encontrado"));

        if (!tipoDietaExistente.getViandas().isEmpty()) {
            throw new IllegalArgumentException("No se puede dar de baja el tipo de dieta porque hay viandas asociadas");
        }

        tipoDietaExistente.setFechaBaja(LocalDate.now());
        tipoDietaRepository.save(tipoDietaExistente);
    }

    public void reactivarTipoDieta(Long id){
        TipoDieta tipoDietaExistente = tipoDietaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo dieta no encontrado"));
        tipoDietaExistente.setFechaBaja(null);
        tipoDietaRepository.save(tipoDietaExistente);
    }

    public List<TipoDieta> obtenerTiposDietas(){
        return tipoDietaRepository.findAll();
    }

    public List<TipoDieta> obtenerTiposDietasActivas(){
        return tipoDietaRepository.findByFechaBajaIsNullOrderByIdDesc();
    }

    public TipoDieta obtenerTipoDietaPorId(Long id){
        return tipoDietaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tipo Dieta no encontrado"));
    }

}
