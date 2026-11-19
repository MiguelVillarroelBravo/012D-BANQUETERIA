package com.paolicias.reservas.controller;

import com.paolicias.reservas.dto.ComprobanteRequest;
import com.paolicias.reservas.dto.EstadoReservaRequest;
import com.paolicias.reservas.dto.ReservaRequest;
import com.paolicias.reservas.entities.Reserva;
import com.paolicias.reservas.services.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    // 1. Crear reserva
    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@Valid @RequestBody ReservaRequest request) {
        Reserva nuevaReserva = reservaService.crearReserva(request);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    // 2. Listar por correo del cliente ("Mis Reservas")
    @GetMapping("/cliente/{email}")
    public ResponseEntity<List<Reserva>> obtenerPorCliente(@PathVariable String email) {
        return ResponseEntity.ok(reservaService.obtenerReservasPorCliente(email));
    }

    // 3. Listar todas con filtros opcionales de estado y fecha (Para Admin)
    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodas(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String fecha) {
        return ResponseEntity.ok(reservaService.listarTodas(estado, fecha));
    }

    // 4. Detalle de una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    // 5. Guardar/Actualizar comprobante
    @PutMapping("/{id}/comprobante")
    public ResponseEntity<Reserva> guardarComprobante(
            @PathVariable Long id,
            @Valid @RequestBody ComprobanteRequest request) {
        return ResponseEntity.ok(reservaService.guardarComprobante(id, request));
    }

    // 6. Cambiar estado (Validar / Rechazar pago)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reserva> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoReservaRequest request) {
        return ResponseEntity.ok(reservaService.cambiarEstado(id, request));
    }
}