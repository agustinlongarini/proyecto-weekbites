package com.longstudios.weekbites.enums;

public enum Frecuencia {

    Lunes, Martes, Miercoles, Jueves, Viernes;

    public static Frecuencia fromString(String nombre) {
        return switch (nombre) {
            case "Lunes" -> Lunes;
            case "Martes" -> Martes;
            case "Miercoles" -> Miercoles;
            case "Jueves" -> Jueves;
            case "Viernes" -> Viernes;
            default -> throw new IllegalArgumentException("Día no válido");
        };
    }
}
