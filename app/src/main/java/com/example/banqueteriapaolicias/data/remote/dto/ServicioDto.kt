package com.example.banqueteriapaolicias.data.remote.dto

data class ServicioDto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precioUnitario: Int,
    val activo: Boolean
)
