package com.paolicias.catalogo.dto;

public record TramoDescuentoResponse(
        Long id,
        String nombreTramo,
        Integer minPersonas,
        Integer maxPersonas,
        Integer porcentajeDescuento
) {
}
