package com.longstudios.weekbites.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vianda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreVianda;
    private String descripcionVianda;
    private String ingredientes;
    private LocalDate fechaBaja;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "vianda_tipo_dieta",
            joinColumns = @JoinColumn(name = "vianda_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_dieta_id")
    )
    private List<TipoDieta> tiposDieta = new ArrayList<>();


}
