package com.longstudios.weekbites.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ModificarSuscripcionDTO {

    // Tipo Dieta
    private Long tipoDietaId;

    // Frecuencia
    private List<String> frecuencias;

    // Forma de entrega
    private String formaEntrega;

    // Dirección
    private String calle;
    private String numero;
    private String piso;
    private String dpto;
    private Long provinciaId;
    private Long localidadId;

    // Otros
    private String comentarios;

}
