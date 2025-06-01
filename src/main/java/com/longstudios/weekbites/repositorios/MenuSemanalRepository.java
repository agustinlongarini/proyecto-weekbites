package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.MenuSemanal;
import com.longstudios.weekbites.entidades.TipoDieta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuSemanalRepository extends JpaRepository<MenuSemanal, Long> {

    List<MenuSemanal> findByTipoDietaId(Long tipoDietaId);
    Optional<MenuSemanal> findByTipoDietaAndSemanaAndAnio(TipoDieta tipoDieta, int semana, int anio);

}
