package com.paolicias.reservas.services;

import com.paolicias.reservas.config.ResourceNotFoundException;
import com.paolicias.reservas.dto.ComprobanteRequest;
import com.paolicias.reservas.dto.EstadoReservaRequest;
import com.paolicias.reservas.dto.ReservaRequest;
import com.paolicias.reservas.entities.Reserva;
import com.paolicias.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    @Override
    public Reserva crearReserva(ReservaRequest request) {
        // Cálculo automático del abono (50% del total de la cotización)
        double abono = request.getTotalCotizacion() * 0.5;

        Reserva reserva = Reserva.builder()
                .clienteEmail(request.getClienteEmail())
                .nombreCliente(request.getNombreCliente())
                .tipoEvento(request.getTipoEvento())
                .fechaEvento(request.getFechaEvento())
                .bloqueHorario(request.getBloqueHorario())
                .cantidadComensales(request.getCantidadComensales())
                .restriccionesAlimentarias(request.getRestriccionesAlimentarias())
                .observaciones(request.getObservaciones())
                .totalCotizacion(request.getTotalCotizacion())
                .abonoRequerido(abono)
                .comprobanteBase64(request.getComprobanteBase64())
                .estado("PENDIENTE")
                .build();

        return reservaRepository.save(reserva);
    }

    @Override
    public List<Reserva> obtenerReservasPorCliente(String email) {
        return reservaRepository.findByClienteEmailOrderByIdDesc(email);
    }

    @Override
    public List<Reserva> listarTodas(String estado, String fecha) {
        if (estado != null && !estado.isEmpty() && fecha != null && !fecha.isEmpty()) {
            return reservaRepository.findByEstadoAndFechaEvento(estado, fecha);
        }
        if (estado != null && !estado.isEmpty()) {
            return reservaRepository.findByEstadoOrderByFechaEventoAsc(estado);
        }
        if (fecha != null && !fecha.isEmpty()) {
            return reservaRepository.findByFechaEventoOrderByIdAsc(fecha);
        }
        return reservaRepository.findAllByOrderByFechaEventoAsc();
    }

    @Override
    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    }

    @Override
    public Reserva guardarComprobante(Long id, ComprobanteRequest request) {
        Reserva reserva = obtenerPorId(id);
        reserva.setComprobanteBase64(request.getComprobanteBase64());
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva cambiarEstado(Long id, EstadoReservaRequest request) {
        Reserva reserva = obtenerPorId(id);
        reserva.setEstado(request.getEstado().toUpperCase());
        return reservaRepository.save(reserva);
    }
}