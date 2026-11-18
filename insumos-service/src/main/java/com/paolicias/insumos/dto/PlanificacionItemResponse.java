package com.paolicias.insumos.dto;

public record PlanificacionItemResponse(
        Long insumoId,
        String nombre,
        String unidad,
        double requerido,
        double stock,
        double faltante,
        long costoEstimado
) {}