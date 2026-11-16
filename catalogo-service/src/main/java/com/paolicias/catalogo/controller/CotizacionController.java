package com.paolicias.catalogo.controller;

import com.paolicias.catalogo.dto.CotizacionRequest;
import com.paolicias.catalogo.dto.CotizacionResponse;
import com.paolicias.catalogo.services.CotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cotizaciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @PostMapping("/calcular")
    public ResponseEntity<CotizacionResponse> calcularCotizacion(@RequestBody CotizacionRequest request) {
        return ResponseEntity.ok(cotizacionService.calcularCotizacion(request));
    }
}
