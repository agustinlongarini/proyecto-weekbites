package com.longstudios.weekbites.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class SuscripcionDTO {

    // Cliente
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String apellido;
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener entre 7 y 8 dígitos")
    private String dni;
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;
    @Pattern(regexp = "^\\d+$", message = "El teléfono solo debe contener números")
    private String telefono;


    // Usuario
    @Email(message = "El correo electrónico no es válido")
    private String email;
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password2;

    // Tipo Dieta
    private Long tipoDietaId;

    // Frecuencia
    @NotEmpty(message = "Debe seleccionar al menos un día de frecuencia")
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
