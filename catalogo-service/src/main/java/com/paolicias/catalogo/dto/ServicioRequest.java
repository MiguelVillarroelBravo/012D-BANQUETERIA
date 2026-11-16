package com.paolicias.catalogo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServicioRequest(
        @NotBlank(message = "El nombre del servicio es obligatorio")
        String nombre,

        @NotBlank(message = "La descripción del servicio es obligatoria")
        String descripcion,

        @NotNull(message = "El precio unitario es obligatorio")
        @Min(value = 5000, message = "El precio unitario debe ser al menos 5000")
        Integer precioUnitario,

        Boolean activo
) {
}
