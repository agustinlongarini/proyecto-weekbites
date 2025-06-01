package com.longstudios.weekbites.enums;

public enum Frecuencia {

    Lunes, Martes, Miercoles, Jueves, Viernes;

    public static Frecuencia fromString(String nombre) {
        String formatted = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        return Frecuencia.valueOf(formatted);
    }
}
