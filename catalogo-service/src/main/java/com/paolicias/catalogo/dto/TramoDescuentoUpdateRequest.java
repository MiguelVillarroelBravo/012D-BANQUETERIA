package com.paolicias.catalogo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TramoDescuentoUpdateRequest(
        @NotNull(message = "El porcentaje de descuento es obligatorio")
        @Min(value = 0, message = "El porcentaje de descuento no puede ser menor a 0")
        @Max(value = 100, message = "El porcentaje de descuento no puede ser mayor a 100")
        Integer porcentajeDescuento
) {
}
