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

    @ManyToOne
    @JoinColumn(name = "vianda_id")
    private Vianda vianda;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
