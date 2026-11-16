package com.paolicias.catalogo.dto;

public record ServicioResponse(
        Long id,
        String nombre,
        String descripcion,
        Integer precioUnitario,
        Boolean activo
) {
}
