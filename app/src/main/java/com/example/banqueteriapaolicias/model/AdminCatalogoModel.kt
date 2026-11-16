package com.example.banqueteriapaolicias.model

import com.example.banqueteriapaolicias.data.remote.dto.ServicioDto

data class AdminCatalogoModel(
    val servicios: List<ServicioDto> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val mensajeExito: String? = null,
    val errorApi: String? = null,
    val servicioEditandoId: Long? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val precioUnitario: String = "",
    val activo: Boolean = true,
    val errorNombre: String? = null,
    val errorDescripcion: String? = null,
    val errorPrecioUnitario: String? = null,
    val precioTorta: String = "",
    val errorPrecioTorta: String? = null,
    val servicioAEliminar: ServicioDto? = null
)
