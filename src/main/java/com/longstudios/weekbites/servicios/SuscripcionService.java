package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.dtos.ModificarSuscripcionDTO;
import com.longstudios.weekbites.dtos.SuscripcionDTO;
import com.longstudios.weekbites.entidades.*;
import com.longstudios.weekbites.enums.FormaEntrega;
import com.longstudios.weekbites.enums.Frecuencia;
import com.longstudios.weekbites.repositorios.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuscripcionService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoDietaRepository tipoDietaRepository;
    private final DireccionRepository direccionRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final HistorialSuscripcionRepository historialSuscripcionRepository;
    private final LocalidadRepository localidadRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public Suscripcion crearSuscripcion(SuscripcionDTO suscripcionDTO){

        // Validación para evitar duplicados
        Optional<Cliente> clienteExistente = clienteRepository.findByDniCliente(suscripcionDTO.getDni());
        if (clienteExistente.isPresent() && clienteExistente.get().getSuscripcion() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente ya tiene una suscripción activa");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(suscripcionDTO.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        //Crea cliente
        Cliente cliente = new Cliente();
        cliente.setNombreCliente(suscripcionDTO.getNombre());
        cliente.setApellidoCliente(suscripcionDTO.getApellido());
        cliente.setDniCliente(suscripcionDTO.getDni());
        cliente.setTelefono(suscripcionDTO.getTelefono());
        cliente.setFechaNacimiento(suscripcionDTO.getFechaNacimiento());

        clienteRepository.save(cliente);

        //Crea usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(suscripcionDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(suscripcionDTO.getPassword()));
        Rol rol = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        usuario.setRol(rol);
        usuario.setCliente(cliente);

        usuarioRepository.save(usuario);

        //Trae Tipo Dieta
        TipoDieta tipoDieta = tipoDietaRepository.findById(suscripcionDTO.getTipoDietaId())
                .orElseThrow(() -> new IllegalArgumentException("TipoDieta no encontrada"));

        //Crea direccion
        if (suscripcionDTO.getFormaEntrega().equals("Domicilio") ||
                suscripcionDTO.getFormaEntrega().equals("Oficina") ||
                suscripcionDTO.getFormaEntrega().equals("Institucion")) {

            Direccion direccion = new Direccion();
            direccion.setCalle(suscripcionDTO.getCalle());
            direccion.setNumero(suscripcionDTO.getNumero());
            direccion.setPiso(suscripcionDTO.getPiso());
            direccion.setDpto(suscripcionDTO.getDpto());
            direccion.setActiva(true);

            Localidad localidad = localidadRepository.findById(suscripcionDTO.getLocalidadId())
                    .orElseThrow(() -> new IllegalArgumentException("Localidad no encontrada"));

            direccion.setLocalidad(localidad);
            direccion.setCliente(cliente);

            direccionRepository.save(direccion);
            cliente.addDireccion(direccion);
        }

        //Trae  Forma de Entrega
        FormaEntrega formaEntrega = FormaEntrega.fromString(suscripcionDTO.getFormaEntrega());

        //Trae frecuencias
        List<Frecuencia> frecuencias = suscripcionDTO.getFrecuencias().stream()
                .map(Frecuencia::fromString)
                .collect(Collectors.toList());

        //Crea suscripcion
        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setEstado(true);
        suscripcion.setFechaInicio(calcularFechaInicioSuscripcion(LocalDate.now(), frecuencias));
        suscripcion.setComentarios(suscripcionDTO.getComentarios());
        suscripcion.setTipoDieta(tipoDieta);
        suscripcion.setFormaEntrega(formaEntrega);
        suscripcion.setFrecuencias(frecuencias);
        suscripcion.setCliente(cliente);

        suscripcionRepository.save(suscripcion);

        //Guardar en el historial
        HistorialSuscripcion historial = new HistorialSuscripcion();
        historial.setFechaCambio(LocalDate.now());
        historial.setTipoDietaNombre(suscripcion.getTipoDieta().getNombreTD());
        historial.setFormaEntrega(suscripcion.getFormaEntrega().toString());
        String frecuenciasComoTexto = suscripcion.getFrecuencias().stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        historial.setFrecuencias(frecuenciasComoTexto);
        historial.setComentarios(suscripcion.getComentarios());
        if (suscripcion.getCliente().getDireccionActual() != null){
            historial.setDireccionResumen(
                    suscripcion.getCliente().getDireccionActual().getCalle() + " " +
                            suscripcion.getCliente().getDireccionActual().getNumero() + ", " +
                            suscripcion.getCliente().getDireccionActual().getLocalidad().getNombre() + ", " +
                            suscripcion.getCliente().getDireccionActual().getLocalidad().getProvincia().getNombreProvincia()
            );
        }
        historial.setSuscripcion(suscripcion);

        historialSuscripcionRepository.save(historial);

        //Autenticacion
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(suscripcionDTO.getEmail(), suscripcionDTO.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return suscripcion;
    }

    @Transactional
    public void modificarSuscripcion(Long id, ModificarSuscripcionDTO modificarSuscripcionDTO) {

        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada"));

        TipoDieta tipoDieta = tipoDietaRepository.findById(modificarSuscripcionDTO.getTipoDietaId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de dieta no encontrada"));

        FormaEntrega nuevaFormaEntrega = FormaEntrega.fromString(modificarSuscripcionDTO.getFormaEntrega());
        List<Frecuencia> nuevasFrecuencias = modificarSuscripcionDTO.getFrecuencias().stream()
                .map(Frecuencia::fromString)
                .collect(Collectors.toList());

        boolean huboCambios = false;

        if (!suscripcion.getTipoDieta().getId().equals(modificarSuscripcionDTO.getTipoDietaId())) {
            suscripcion.setTipoDieta(tipoDieta);
            huboCambios = true;
        }

        if (!suscripcion.getFormaEntrega().equals(nuevaFormaEntrega)) {
            suscripcion.setFormaEntrega(nuevaFormaEntrega);
            huboCambios = true;
        }

        if (!new HashSet<>(suscripcion.getFrecuencias()).equals(new HashSet<>(nuevasFrecuencias))) {
            suscripcion.setFrecuencias(nuevasFrecuencias);
            huboCambios = true;
        }

        if (modificarSuscripcionDTO.getComentarios() != null &&
                !modificarSuscripcionDTO.getComentarios().equals(suscripcion.getComentarios())) {
            suscripcion.setComentarios(modificarSuscripcionDTO.getComentarios());
            huboCambios = true;
        }

        if (modificarSuscripcionDTO.getFormaEntrega().equals("Domicilio") ||
                modificarSuscripcionDTO.getFormaEntrega().equals("Oficina") ||
                modificarSuscripcionDTO.getFormaEntrega().equals("Institucion")) {

            Direccion direccionActual = suscripcion.getCliente().getDireccionActual();

            boolean direccionCambio = direccionActual == null ||
                    !Objects.equals(direccionActual.getCalle(), modificarSuscripcionDTO.getCalle()) ||
                    !Objects.equals(direccionActual.getNumero(), modificarSuscripcionDTO.getNumero()) ||
                    !Objects.equals(direccionActual.getPiso(), modificarSuscripcionDTO.getPiso()) ||
                    !Objects.equals(direccionActual.getDpto(), modificarSuscripcionDTO.getDpto()) ||
                    !Objects.equals(direccionActual.getLocalidad().getId(), modificarSuscripcionDTO.getLocalidadId());

            if (direccionCambio) {
                suscripcion.getCliente().getDirecciones().forEach(d -> {
                    if (d.isActiva()) {
                        d.setActiva(false);
                        direccionRepository.save(d);
                    }
                });

                Direccion nuevaDireccion = new Direccion();
                nuevaDireccion.setCalle(modificarSuscripcionDTO.getCalle());
                nuevaDireccion.setNumero(modificarSuscripcionDTO.getNumero());
                nuevaDireccion.setPiso(modificarSuscripcionDTO.getPiso());
                nuevaDireccion.setDpto(modificarSuscripcionDTO.getDpto());
                nuevaDireccion.setActiva(true);

                Localidad localidad = localidadRepository.findById(modificarSuscripcionDTO.getLocalidadId())
                        .orElseThrow(() -> new IllegalArgumentException("Localidad no encontrada"));
                nuevaDireccion.setLocalidad(localidad);
                nuevaDireccion.setCliente(suscripcion.getCliente());

                direccionRepository.save(nuevaDireccion);
                suscripcion.getCliente().addDireccion(nuevaDireccion);

                huboCambios = true;
            }
        } // que hacer si tenia direccion y cambia a retiro en sucursal

        if (huboCambios) {
            suscripcionRepository.save(suscripcion);

            HistorialSuscripcion historial = new HistorialSuscripcion();
            historial.setFechaCambio(LocalDate.now());
            historial.setTipoDietaNombre(suscripcion.getTipoDieta().getNombreTD());
            historial.setFormaEntrega(suscripcion.getFormaEntrega().toString());
            String frecuenciasComoTexto = suscripcion.getFrecuencias().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            historial.setFrecuencias(frecuenciasComoTexto);
            historial.setComentarios(suscripcion.getComentarios());
            if (suscripcion.getCliente().getDireccionActual() != null){
                historial.setDireccionResumen(
                        suscripcion.getCliente().getDireccionActual().getCalle() + " " +
                                suscripcion.getCliente().getDireccionActual().getNumero() + ", " +
                                suscripcion.getCliente().getDireccionActual().getLocalidad().getNombre() + ", " +
                                suscripcion.getCliente().getDireccionActual().getLocalidad().getProvincia().getNombreProvincia()
                );
            }
            historial.setSuscripcion(suscripcion);
            historialSuscripcionRepository.save(historial);
        }
    }

    @Transactional
    public void suspenderSuscripcion(Long id){
        Suscripcion suscripcionASuspender = suscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada"));
        if (suscripcionASuspender.getFechaInicio().isAfter(LocalDate.now())) {
            throw new IllegalStateException("No se puede suspender una suscripción que aún no comenzó.");
        }
        if (!suscripcionASuspender.isEstado()) {
            throw new IllegalStateException("La suscripción ya está suspendida.");
        }
        suscripcionASuspender.setEstado(false);
        suscripcionASuspender.setFechaSuspension(LocalDate.now());
        suscripcionRepository.save(suscripcionASuspender);
    }

    @Transactional
    public void reactivarSuscripcion(Long id){
        Suscripcion suscripcionAReactivar = suscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada"));
        if (suscripcionAReactivar.isEstado()) {
            throw new IllegalStateException("La suscripción ya está activa.");
        }
        suscripcionAReactivar.setEstado(true);
        suscripcionAReactivar.setFechaSuspension(null);
        suscripcionRepository.save(suscripcionAReactivar);
    }

    public List<HistorialSuscripcion> consultarHistorialSuscripciones(Long id){
        if (!suscripcionRepository.existsById(id)) {
            throw new IllegalArgumentException("Suscripción no encontrada");
        }
        return historialSuscripcionRepository.findBySuscripcionIdOrderByFechaCambioDesc(id);
    }

    public LocalDate calcularFechaInicioSuscripcion(LocalDate fechaSuscripcion, List<Frecuencia> diasElegidos) {
        LocalDate inicioSemana = fechaSuscripcion.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        for (int i = 0; i < 7; i++) {
            LocalDate posibleDia = inicioSemana.plusDays(i);
            Frecuencia diaDeLaSemana = mapearDiaDeLaSemana(posibleDia.getDayOfWeek());
            if (diasElegidos.contains(diaDeLaSemana)) {
                return posibleDia;
            }
        }
        throw new IllegalStateException("No se encontró un día válido para iniciar la suscripción.");
    }

    private Frecuencia mapearDiaDeLaSemana(DayOfWeek diaDeLaSemana) {
        switch (diaDeLaSemana) {
            case MONDAY: return Frecuencia.Lunes;
            case TUESDAY: return Frecuencia.Martes;
            case WEDNESDAY: return Frecuencia.Miercoles;
            case THURSDAY: return Frecuencia.Jueves;
            case FRIDAY: return Frecuencia.Viernes;
            default: throw new IllegalArgumentException("Día no válido: " + diaDeLaSemana);
        }
    }

    public Suscripcion obtenerSuscripcionPorId(Long id){
        return suscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suscripción no encontrada"));
    }

    public List<Frecuencia> obtenerFrecuencias() {
        return List.of(Frecuencia.Lunes, Frecuencia.Martes, Frecuencia.Miercoles, Frecuencia.Jueves, Frecuencia.Viernes);
    }

}
