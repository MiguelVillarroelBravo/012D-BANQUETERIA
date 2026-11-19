package com.paolicias.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ComprobanteRequest {

    @NotBlank(message = "La imagen del comprobante no puede estar vacía")
    private String comprobanteBase64;
}