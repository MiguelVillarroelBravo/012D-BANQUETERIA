package com.paolicias.reservas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReservaRequest {

    @NotBlank(message = "El correo del cliente es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String clienteEmail;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    @NotBlank(message = "El tipo de evento es obligatorio")
    private String tipoEvento;

    @NotBlank(message = "La fecha del evento es obligatoria")
    private String fechaEvento;

    private String bloqueHorario;

    @Min(value = 1, message = "Debe haber al menos 1 comensal")
    private int cantidadComensales;

    private String restriccionesAlimentarias;
    private String observaciones;

    @Min(value = 1, message = "El total de la cotización debe ser mayor a 0")
    private double totalCotizacion;

    private double abonoRequerido;
    private String comprobanteBase64; // Opcional al crear, o se puede enviar directo aquí
}