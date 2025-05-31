package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {
    List<Localidad> findByProvinciaId(int provinciaId);
}
