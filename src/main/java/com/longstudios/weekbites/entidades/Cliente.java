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
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dniCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String telefono;
    private LocalDate fechaNacimiento;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();

    @OneToOne(mappedBy = "cliente")
    private Suscripcion suscripcion;

    @OneToOne(mappedBy = "cliente")
    private Usuario usuario;

    public void addDireccion(Direccion direccion){
        direccion.setCliente(this);
        direcciones.add(direccion);
    }

    public Direccion getDireccionActual() {
        return direcciones.stream()
                .filter(Direccion::isActiva)
                .findFirst()
                .orElse(null);
    }

}
