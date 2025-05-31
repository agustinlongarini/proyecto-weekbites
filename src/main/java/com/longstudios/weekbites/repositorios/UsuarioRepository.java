package com.longstudios.weekbites.repositorios;

import com.longstudios.weekbites.entidades.Rol;
import com.longstudios.weekbites.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByRolAndFechaBajaIsNull(Rol rol);

}

