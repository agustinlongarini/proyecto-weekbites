package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.DiaMenu;
import com.longstudios.weekbites.enums.Frecuencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaMenuRepository extends JpaRepository<DiaMenu, Long> {

    List<DiaMenu> findByMenuSemanalId(Long menuId);
    List<DiaMenu> findByDiaSemanaAndMenuSemanalId(Frecuencia dia, Long menuId);

}
