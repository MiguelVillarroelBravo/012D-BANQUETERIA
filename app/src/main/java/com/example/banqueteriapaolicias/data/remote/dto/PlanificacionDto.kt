package com.example.banqueteriapaolicias.data.remote.dto

data class PlanificacionItemDto(
    val insumoId: Long,
    val nombre: String,
    val unidad: String,
    val requerido: Double,
    val stock: Double,
    val faltante: Double,
    val costoEstimado: Long
)

data class PlanificacionDto(
    val personas: Int,
    val items: List<PlanificacionItemDto>,
    val costoTotalEstimado: Long
)