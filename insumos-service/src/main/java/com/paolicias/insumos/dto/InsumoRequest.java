package com.paolicias.insumos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InsumoRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La categoría es obligatoria") String categoria,
        @NotBlank(message = "La unidad es obligatoria") String unidad,
        @NotNull(message = "El stock es obligatorio") @PositiveOrZero(message = "El stock no puede ser negativo") Double stock,
        @NotNull(message = "La cantidad por persona es obligatoria") @PositiveOrZero(message = "La cantidad por persona no puede ser negativa") Double cantidadPorPersona,
        @NotNull(message = "El costo unitario es obligatorio") @PositiveOrZero(message = "El costo unitario no puede ser negativo") Double costoUnitario
) {}