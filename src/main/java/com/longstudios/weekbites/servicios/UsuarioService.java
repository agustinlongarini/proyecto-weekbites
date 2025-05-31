package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.Cliente;
import com.longstudios.weekbites.entidades.Suscripcion;
import com.longstudios.weekbites.entidades.Usuario;
import com.longstudios.weekbites.repositorios.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void crearUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    public void modificarUsuario(Long id, Usuario usuarioModificado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuarioExistente.setEmail(usuarioModificado.getEmail());
        usuarioExistente.setPassword(usuarioModificado.getPassword());
        usuarioExistente.setRol(usuarioModificado.getRol());
        usuarioRepository.save(usuarioExistente);
    }

    public void darDeBajaUsuario(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Cliente cliente = usuarioExistente.getCliente();

        if (cliente != null && cliente.getSuscripcion() != null) {
            Suscripcion suscripcion = cliente.getSuscripcion();
            if (suscripcion.getFechaSuspension() == null) {
                throw new IllegalArgumentException("No se puede dar de baja un cliente con una suscripción activa");
            }
        }

        usuarioExistente.setFechaBaja(LocalDate.now());
        usuarioRepository.save(usuarioExistente);
    }

    public void reactivarUsuario(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioExistente.setFechaBaja(null);
        usuarioRepository.save(usuarioExistente);
    }

    public void cambiarContrasenia(Long id, String passwordActual, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        if (passwordEncoder.matches(nuevaPassword, usuario.getPassword())) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la anterior.");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    public Usuario buscarUsuarioPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    public List<Usuario> obtenerUsuarios(){
        return usuarioRepository.findAll();
    }

}
