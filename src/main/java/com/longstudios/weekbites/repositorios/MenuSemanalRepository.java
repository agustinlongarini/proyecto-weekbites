package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.MenuSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuSemanalRepository extends JpaRepository<MenuSemanal, Long> {

    List<MenuSemanal> findByTipoDietaId(Long tipoDietaId);
    List<MenuSemanal> findByFechaBajaIsNullOrderByIdDesc();
    Optional<MenuSemanal> findBySemanaAndTipoDietaIdAndFechaBajaIsNull(int semana, Long tipoDietaId);
    List<MenuSemanal> findAllByOrderByIdDesc();

}
