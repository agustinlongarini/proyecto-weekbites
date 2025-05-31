package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.HistorialSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialSuscripcionRepository extends JpaRepository<HistorialSuscripcion, Long> {
    List<HistorialSuscripcion> findBySuscripcionIdOrderByFechaCambioDesc(Long suscripcionId);
}
