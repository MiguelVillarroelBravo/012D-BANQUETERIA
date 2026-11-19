package com.paolicias.reservas.services;

import com.paolicias.reservas.dto.EstadoReservaRequest;
import com.paolicias.reservas.dto.ReservaRequest;
import com.paolicias.reservas.entities.Reserva;
import com.paolicias.reservas.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearReservaCalculaAbonoCorrectamente() {
        ReservaRequest request = new ReservaRequest();
        request.setClienteEmail("test@correo.com");
        request.setNombreCliente("Juan Pérez");
        request.setTipoEvento("Matrimonio");
        request.setFechaEvento("2026-12-15");
        request.setCantidadComensales(50);
        request.setTotalCotizacion(200000.0);

        Reserva reservaGuardada = Reserva.builder()
                .id(1L)
                .clienteEmail(request.getClienteEmail())
                .nombreCliente(request.getNombreCliente())
                .tipoEvento(request.getTipoEvento())
                .fechaEvento(request.getFechaEvento())
                .totalCotizacion(200000.0)
                .abonoRequerido(100000.0)
                .estado("PENDIENTE")
                .build();

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaGuardada);

        Reserva resultado = reservaService.crearReserva(request);

        assertNotNull(resultado);
        assertEquals(100000.0, resultado.getAbonoRequerido());
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void testCambiarEstadoReserva() {
        Reserva reservaExistente = Reserva.builder()
                .id(1L)
                .estado("PENDIENTE")
                .build();

        EstadoReservaRequest estadoRequest = new EstadoReservaRequest();
        estadoRequest.setEstado("VALIDADO");

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reserva resultado = reservaService.cambiarEstado(1L, estadoRequest);

        assertEquals("VALIDADO", resultado.getEstado());
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }
}