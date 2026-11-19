package com.paolicias.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstadoReservaRequest {

    @NotBlank(message = "El nuevo estado es obligatorio (VALIDADO o RECHAZADO)")
    private String estado;
}