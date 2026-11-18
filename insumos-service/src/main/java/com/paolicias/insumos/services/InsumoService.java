package com.paolicias.insumos.services;

import com.paolicias.insumos.dto.InsumoRequest;
import com.paolicias.insumos.dto.PlanificacionResponse;
import com.paolicias.insumos.entities.Insumo;

import java.util.List;

public interface InsumoService {
    List<Insumo> listar();
    Insumo obtener(Long id);
    Insumo crear(InsumoRequest request);
    Insumo actualizar(Long id, InsumoRequest request);
    void eliminar(Long id);
    PlanificacionResponse planificar(int personas);
}