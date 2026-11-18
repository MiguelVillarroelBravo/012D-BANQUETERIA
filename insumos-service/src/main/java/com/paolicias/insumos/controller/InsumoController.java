package com.paolicias.insumos.controller;

import com.paolicias.insumos.dto.InsumoRequest;
import com.paolicias.insumos.dto.PlanificacionResponse;
import com.paolicias.insumos.entities.Insumo;
import com.paolicias.insumos.services.InsumoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/insumos")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @GetMapping
    public List<Insumo> listar() {
        return insumoService.listar();
    }

    @GetMapping("/planificacion")
    public PlanificacionResponse planificar(@RequestParam int personas) {
        return insumoService.planificar(personas);
    }

    @GetMapping("/{id}")
    public Insumo obtener(@PathVariable Long id) {
        return insumoService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Insumo crear(@Valid @RequestBody InsumoRequest request) {
        return insumoService.crear(request);
    }

    @PutMapping("/{id}")
    public Insumo actualizar(@PathVariable Long id, @Valid @RequestBody InsumoRequest request) {
        return insumoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        insumoService.eliminar(id);
    }
}