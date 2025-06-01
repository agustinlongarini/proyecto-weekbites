package com.longstudios.weekbites.servicios;

import com.longstudios.weekbites.entidades.*;
import com.longstudios.weekbites.enums.EstadoEntrega;
import com.longstudios.weekbites.enums.FormaEntrega;
import com.longstudios.weekbites.enums.Frecuencia;
import com.longstudios.weekbites.repositorios.EntregaRepository;
import com.longstudios.weekbites.repositorios.MenuSemanalRepository;
import com.longstudios.weekbites.repositorios.SuscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final MenuSemanalRepository menuSemanalRepository;

    public void generarEntregasParaSemana(int anio, int semana) {
        LocalDate fechaInicioSemana = getFechaLunesDeSemana(anio, semana);
        LocalDate hoy = LocalDate.now();

        if (fechaInicioSemana.isBefore(hoy.with(DayOfWeek.MONDAY))) {
            throw new IllegalArgumentException("No se pueden generar entregas para semanas anteriores.");
        }

        List<Suscripcion> suscripciones = suscripcionRepository.findAll();
        boolean algunMenuEncontrado = false;

        for (Suscripcion suscripcion : suscripciones) {
            if (suscripcion.getFechaSuspension() != null &&
                    !suscripcion.getFechaSuspension().isAfter(fechaInicioSemana)) {
                continue;
            }

            Cliente cliente = suscripcion.getCliente();
            TipoDieta tipoDieta = suscripcion.getTipoDieta();
            FormaEntrega forma = suscripcion.getFormaEntrega();

            Optional<MenuSemanal> optionalMenu = menuSemanalRepository.findByTipoDietaAndSemanaAndAnio(tipoDieta, semana, anio);
            if (optionalMenu.isEmpty()) continue;

            algunMenuEncontrado = true;
            MenuSemanal menu = optionalMenu.get();
            Map<String, Vianda> viandasPorDia = menu.getViandasPorDia();

            for (Map.Entry<String, Vianda> entry : viandasPorDia.entrySet()) {
                String diaStr = entry.getKey();
                Vianda vianda = entry.getValue();

                Frecuencia dia;
                try {
                    dia = Frecuencia.fromString(diaStr);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                LocalDate fechaEntrega = fechaInicioSemana.plusDays(dia.ordinal());

                boolean yaExiste = entregaRepository.existsByClienteAndFechaEntrega(cliente, fechaEntrega);
                if (yaExiste) continue;

                Entrega entrega = new Entrega();
                entrega.setCliente(cliente);
                entrega.setVianda(vianda);
                entrega.setFechaEntrega(fechaEntrega);
                entrega.setFormaEntrega(forma);
                entrega.setEstado(EstadoEntrega.PENDIENTE);

                entregaRepository.save(entrega);
            }
        }

        if (!algunMenuEncontrado) {
            throw new IllegalArgumentException("No hay menús cargados para la semana seleccionada.");
        }
    }


    public List<Entrega> obtenerTodas(){
        return entregaRepository.findAll();
    }

    public LocalDate getFechaLunesDeSemana(int anio, int semana) {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfYear();
        return LocalDate.of(anio, 1, 1)
                .with(weekOfYear, semana)
                .with(DayOfWeek.MONDAY);
    }

}
