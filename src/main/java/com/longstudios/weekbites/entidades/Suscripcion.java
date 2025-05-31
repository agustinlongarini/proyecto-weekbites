package com.longstudios.weekbites.entidades;

import com.longstudios.weekbites.enums.FormaEntrega;
import com.longstudios.weekbites.enums.Frecuencia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean estado;
    private LocalDate fechaInicio;
    private LocalDate fechaSuspension;
    private String comentarios;

    @ManyToOne
    @JoinColumn(name = "tipo_dieta_id")
    private TipoDieta tipoDieta;

    @Enumerated(EnumType.STRING)
    private FormaEntrega formaEntrega;

    @ElementCollection(targetClass = Frecuencia.class)
    @CollectionTable(name = "suscripcion_frecuencia")
    @Enumerated(EnumType.STRING)
    private List<Frecuencia> frecuencias;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
