package com.paolicias.reservas.services;

import com.paolicias.reservas.dto.ComprobanteRequest;
import com.paolicias.reservas.dto.EstadoReservaRequest;
import com.paolicias.reservas.dto.ReservaRequest;
import com.paolicias.reservas.entities.Reserva;

import java.util.List;

public interface ReservaService {
    Reserva crearReserva(ReservaRequest request);
    List<Reserva> obtenerReservasPorCliente(String email);
    List<Reserva> listarTodas(String estado, String fecha);
    Reserva obtenerPorId(Long id);
    Reserva guardarComprobante(Long id, ComprobanteRequest request);
    Reserva cambiarEstado(Long id, EstadoReservaRequest request);
}