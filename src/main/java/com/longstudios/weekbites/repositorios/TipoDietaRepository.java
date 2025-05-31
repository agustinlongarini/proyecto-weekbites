package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.TipoDieta;
import com.longstudios.weekbites.entidades.Vianda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoDietaRepository extends JpaRepository<TipoDieta, Long> {

    List<TipoDieta> findByFechaBajaIsNullOrderByIdDesc();

}
