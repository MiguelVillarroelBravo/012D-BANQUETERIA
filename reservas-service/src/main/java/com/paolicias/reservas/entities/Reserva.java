package com.paolicias.reservas.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clienteEmail;

    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String tipoEvento;

    @Column(nullable = false)
    private String fechaEvento; // Formato DD/MM/AAAA o YYYY-MM-DD

    private String bloqueHorario;
    private int cantidadComensales;
    private String restriccionesAlimentarias;
    private String observaciones;

    private double totalCotizacion;
    private double abonoRequerido;

    @Column(columnDefinition = "TEXT")
    private String comprobanteBase64; // Foto del voucher capturada por el cliente

    @Column(nullable = false)
    @Builder.Default
    private String estado = "PENDIENTE"; // PENDIENTE, VALIDADO, RECHAZADO

    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}