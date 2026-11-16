package com.example.banqueteriapaolicias.data.remote.dto

data class ServicioRequestDto(
    val nombre: String,
    val descripcion: String,
    val precioUnitario: Int,
    val activo: Boolean = true
)
