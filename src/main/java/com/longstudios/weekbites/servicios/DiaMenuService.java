package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.DiaMenu;
import com.longstudios.weekbites.repositorios.DiaMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaMenuService {

    private final DiaMenuRepository diaMenuRepository;

    public List<DiaMenu> obtenerPorMenu(Long menuId) {
        return diaMenuRepository.findByMenuSemanalId(menuId);
    }

    public DiaMenu guardarDiaMenu(DiaMenu diaMenu) {
        return diaMenuRepository.save(diaMenu);
    }

}
