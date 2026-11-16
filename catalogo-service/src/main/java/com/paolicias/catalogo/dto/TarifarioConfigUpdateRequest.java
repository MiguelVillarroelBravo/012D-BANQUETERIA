package com.paolicias.catalogo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TarifarioConfigUpdateRequest(
        @NotNull(message = "El precio de torta por persona es obligatorio")
        @Min(value = 0, message = "El precio de torta por persona debe ser mayor o igual a 0")
        Integer precioTortaPorPersona
) {
}
