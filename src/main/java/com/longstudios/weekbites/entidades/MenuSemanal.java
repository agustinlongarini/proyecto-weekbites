package com.longstudios.weekbites.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int semana;
    private int anio;
    private LocalDate fechaBaja;

    @ManyToOne
    @JoinColumn(name = "tipo_dieta_id")
    private TipoDieta tipoDieta;

    @OneToMany(mappedBy = "menuSemanal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaMenu> dias = new ArrayList<>();

    @Transient
    public Map<String, Vianda> getViandasPorDia() {
        Map<String, Vianda> map = new HashMap<>();
        for (DiaMenu dia : dias) {
            if (dia.getDiaSemana() != null && dia.getVianda() != null) {
                map.put(dia.getDiaSemana().name().toLowerCase(), dia.getVianda());
            }
        }
        return map;
    }

}
