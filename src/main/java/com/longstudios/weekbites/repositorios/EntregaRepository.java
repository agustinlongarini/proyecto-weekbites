package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.Cliente;
import com.longstudios.weekbites.entidades.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    boolean existsByClienteAndFechaEntrega(Cliente cliente, LocalDate fechaEntrega);

}
