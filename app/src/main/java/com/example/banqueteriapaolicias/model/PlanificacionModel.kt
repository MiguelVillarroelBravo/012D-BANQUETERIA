package com.example.banqueteriapaolicias.model

import com.example.banqueteriapaolicias.data.remote.dto.PlanificacionDto

data class PlanificacionModel(
    val personas: String = "",
    val errorPersonas: String? = null,
    val isLoading: Boolean = false,
    val resultado: PlanificacionDto? = null,
    val errorApi: String? = null
)