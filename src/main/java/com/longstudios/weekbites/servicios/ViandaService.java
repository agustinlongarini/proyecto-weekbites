package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.Vianda;
import com.longstudios.weekbites.repositorios.ViandaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViandaService {

    private final ViandaRepository viandaRepository;

    public void crearVianda(Vianda vianda){
        viandaRepository.save(vianda);
    }

    public void modificarVianda(Long id, Vianda viandaModificada){
        Vianda viandaExistente = viandaRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Vianda no encontrada"));
        viandaExistente.setNombreVianda(viandaModificada.getNombreVianda());
        viandaExistente.setDescripcionVianda(viandaModificada.getDescripcionVianda());
        viandaExistente.setIngredientes(viandaModificada.getIngredientes());
        viandaExistente.setTiposDieta(viandaModificada.getTiposDieta());
        viandaRepository.save(viandaExistente);
    }

    public void darDeBajaVianda(Long id){
        Vianda viandaExistente = viandaRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Vianda no encontrada"));
        viandaExistente.setFechaBaja(LocalDate.now());
        viandaRepository.save(viandaExistente);
    }

    public void reactivarVianda(Long id){
        Vianda viandaExistente = viandaRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Vianda no encontrada"));
        viandaExistente.setFechaBaja(null);
        viandaRepository.save(viandaExistente);
    }

    public List<Vianda> obtenerViandas(){

        return viandaRepository.findAll();
    }

    public List<Vianda> obtenerViandasActivas(){

        return viandaRepository.findByFechaBajaIsNullOrderByIdDesc();
    }

    public Vianda obtenerViandaPorId(Long id){
        return viandaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Vianda no encontrada"));
    }

    public List<Vianda> buscarViandaPorNombre(String nombre) {
        return viandaRepository.findByNombreViandaContainingIgnoreCase(nombre);
    }

    public List<Vianda> obtenerViandasPorTipoDietaActivas(Long tipoDietaId) {
        if (tipoDietaId == null) {
            throw new IllegalArgumentException("El ID del tipo de dieta no puede ser nulo.");
        }
        return viandaRepository.findActivasByTipoDietaId(tipoDietaId);
    }

}
