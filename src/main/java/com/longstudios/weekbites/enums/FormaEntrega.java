package com.longstudios.weekbites.enums;

public enum FormaEntrega {
    Sucursal, Domicilio, Oficina, Institucion;

    public static FormaEntrega fromString(String formaEntrega) {
        switch (formaEntrega) {
            case "Sucursal":
                return Sucursal;
            case "Domicilio":
                return Domicilio;
            case "Oficina":
                return Oficina;
            case "Institucion":
                return Institucion;
            default:
                throw new IllegalArgumentException("Frecuencia no válida: " + formaEntrega);
        }
    }


}
