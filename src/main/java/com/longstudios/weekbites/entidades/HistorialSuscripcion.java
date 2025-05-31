package com.longstudios.weekbites.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaCambio;

    private String tipoDietaNombre;
    private String formaEntrega;
    private String comentarios;
    private String frecuencias;
    private String direccionResumen;

    @ManyToOne
    @JoinColumn(name = "suscripcion_id")
    private Suscripcion suscripcion;

}
