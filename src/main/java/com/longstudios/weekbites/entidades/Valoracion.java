package com.longstudios.weekbites.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double puntuacion;
    private String comentario;

    @OneToOne
    @JoinColumn(name = "entrega_id", nullable = false)
    private Entrega entrega;

}
