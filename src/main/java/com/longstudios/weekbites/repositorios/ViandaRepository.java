package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.Vianda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViandaRepository extends JpaRepository<Vianda, Long> {

    List<Vianda> findByNombreViandaContainingIgnoreCase(String keyword);

    List<Vianda> findByFechaBajaIsNullOrderByIdDesc();

    @Query("SELECT v FROM Vianda v JOIN v.tiposDieta td WHERE v.fechaBaja IS NULL AND td.id = :tipoDietaId ORDER BY v.nombreVianda ASC")
    List<Vianda> findActivasByTipoDietaId(@Param("tipoDietaId") Long tipoDietaId);

}
