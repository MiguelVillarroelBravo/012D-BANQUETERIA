package com.paolicias.catalogo.controller;

import com.paolicias.catalogo.dto.TarifarioConfigResponse;
import com.paolicias.catalogo.dto.TarifarioConfigUpdateRequest;
import com.paolicias.catalogo.dto.TramoDescuentoResponse;
import com.paolicias.catalogo.dto.TramoDescuentoUpdateRequest;
import com.paolicias.catalogo.services.TarifarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tarifario")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TarifarioController {

    private final TarifarioService tarifarioService;

    @GetMapping("/tramos")
    public ResponseEntity<List<TramoDescuentoResponse>> listarTramos() {
        return ResponseEntity.ok(tarifarioService.listarTramos());
    }

    @PutMapping("/tramos/{id}")
    public ResponseEntity<TramoDescuentoResponse> actualizarPorcentajeTramo(
            @PathVariable Long id,
            @Valid @RequestBody TramoDescuentoUpdateRequest request) {
        return ResponseEntity.ok(tarifarioService.actualizarPorcentajeTramo(id, request));
    }

    @GetMapping("/config")
    public ResponseEntity<TarifarioConfigResponse> obtenerConfiguracion() {
        return ResponseEntity.ok(tarifarioService.obtenerConfiguracion());
    }

    @PutMapping("/config")
    public ResponseEntity<TarifarioConfigResponse> actualizarConfiguracion(
            @Valid @RequestBody TarifarioConfigUpdateRequest request) {
        return ResponseEntity.ok(tarifarioService.actualizarConfiguracion(request));
    }
}
