package com.longstudios.weekbites.entidades;

import com.longstudios.weekbites.enums.EstadoEntrega;
import com.longstudios.weekbites.enums.FormaEntrega;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEntrega;

    @Enumerated(EnumType.STRING)
    private EstadoEntrega estado;

    @Enumerated(EnumType.STRING)
    private FormaEntrega formaEntrega;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vianda_id", nullable = false)
    private Vianda vianda;

    @ManyToOne
    @JoinColumn(name = "cocinero_id")
    private Usuario cocinero;

    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Usuario repartidor;

}
