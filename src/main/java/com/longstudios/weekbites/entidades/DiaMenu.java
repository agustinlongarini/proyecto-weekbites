package com.longstudios.weekbites.entidades;

import com.longstudios.weekbites.enums.Frecuencia;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Frecuencia diaSemana;

    @ManyToOne
    @JoinColumn(name = "vianda_id")
    private Vianda vianda;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuSemanal menuSemanal;
}