package com.example.banqueteriapaolicias.model

import com.example.banqueteriapaolicias.data.remote.dto.ServicioDto

data class CatalogoModel(
    val servicios: List<ServicioDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val servicioSeleccionado: String = "",
    val precioSeleccionado: Int = 0
)
