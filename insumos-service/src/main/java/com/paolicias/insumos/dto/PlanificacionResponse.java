package com.paolicias.insumos.dto;

import java.util.List;

public record PlanificacionResponse(
        int personas,
        List<PlanificacionItemResponse> items,
        long costoTotalEstimado
) {}