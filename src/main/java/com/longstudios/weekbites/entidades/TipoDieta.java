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
public class TipoDieta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreTD;
    private String descripcionTD;
    private LocalDate fechaBaja;

    @ToString.Exclude
    @ManyToMany(mappedBy = "tiposDieta")
    private List<Vianda> viandas = new ArrayList<>();

}
