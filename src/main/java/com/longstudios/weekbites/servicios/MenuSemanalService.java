package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.DiaMenu;
import com.longstudios.weekbites.entidades.MenuSemanal;
import com.longstudios.weekbites.entidades.TipoDieta;
import com.longstudios.weekbites.entidades.Vianda;
import com.longstudios.weekbites.enums.Frecuencia;
import com.longstudios.weekbites.repositorios.MenuSemanalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuSemanalService {

    private final MenuSemanalRepository menuSemanalRepository;
    private final TipoDietaService tipoDietaService;
    private final ViandaService viandaService;

    public void crearMenu(int semana, Long tipoDietaId, Map<String, String[]> parametros) {
        TipoDieta tipoDieta = tipoDietaService.obtenerTipoDietaPorId(tipoDietaId);
        MenuSemanal menu = new MenuSemanal();
        menu.setSemana(semana);
        menu.setTipoDieta(tipoDieta);
        List<DiaMenu> dias = new ArrayList<>();
        for (Frecuencia dia : Frecuencia.values()) {
            String key = "dia_" + dia.name().toLowerCase() + "ViandaId";
            String[] viandaIdArr = parametros.get(key);
            if (viandaIdArr != null && viandaIdArr.length > 0 && !viandaIdArr[0].isBlank()) {
                try {
                    Long viandaId = Long.parseLong(viandaIdArr[0].trim());
                    Vianda vianda = viandaService.obtenerViandaPorId(viandaId);
                    DiaMenu diaMenu = new DiaMenu();
                    diaMenu.setDiaSemana(dia);
                    diaMenu.setMenuSemanal(menu);
                    diaMenu.setVianda(vianda);
                    dias.add(diaMenu);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("ID de vianda inválido para el día " + dia.name());
                }
            }
        }
        if (dias.size() < Frecuencia.values().length) {
            throw new IllegalArgumentException("Debes asignar una vianda a cada día de la semana.");
        }
        menu.setDias(dias);
        menuSemanalRepository.save(menu);
    }

    public void modificarMenu(Long id, Map<String, String[]> parametros) {
        MenuSemanal menu = obtenerMenuSemanalPorId(id);
        List<DiaMenu> diasManagedCollection = menu.getDias();
        diasManagedCollection.clear();
        for (Frecuencia dia : Frecuencia.values()) {
            String key = "dia_" + dia.name().toLowerCase() + "ViandaId";
            String[] viandaIdArr = parametros.get(key);
            if (viandaIdArr != null && viandaIdArr.length > 0 && !viandaIdArr[0].isBlank()) {
                try {
                    Long viandaId = Long.parseLong(viandaIdArr[0].trim());
                    Vianda vianda = viandaService.obtenerViandaPorId(viandaId);
                    DiaMenu diaMenu = new DiaMenu();
                    diaMenu.setDiaSemana(dia);
                    diaMenu.setMenuSemanal(menu);
                    diaMenu.setVianda(vianda);
                    diasManagedCollection.add(diaMenu);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("ID de vianda inválido para el día " + dia.name());
                }
            }
        }
        if (diasManagedCollection.size() < Frecuencia.values().length) {
            throw new IllegalArgumentException("Debes asignar una vianda a cada día de la semana.");
        }
        menuSemanalRepository.save(menu);
    }

    public void darDeBajaMenu(Long id) {
        MenuSemanal menu = obtenerMenuSemanalPorId(id);
        if (menu.getFechaBaja() == null) {
            menu.setFechaBaja(LocalDate.now());
            menuSemanalRepository.save(menu);
        }
    }

    public void reactivarMenu(Long id) {
        MenuSemanal menu = obtenerMenuSemanalPorId(id);
        if (menu.getFechaBaja() != null) {
            menu.setFechaBaja(null);
            menuSemanalRepository.save(menu);
        }
    }

    public List<MenuSemanal> obtenerMenuSemanales() {
        return menuSemanalRepository.findAll();
    }

    public List<MenuSemanal> obtenerPorTipoDieta(Long tipoDietaId){
        return menuSemanalRepository.findByTipoDietaId(tipoDietaId);
    }

    public MenuSemanal obtenerMenuSemanalPorId(Long id){
        return menuSemanalRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Menu no encontrado"));
    }

}