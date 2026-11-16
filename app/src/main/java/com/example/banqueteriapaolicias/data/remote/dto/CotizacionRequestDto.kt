package com.example.banqueteriapaolicias.data.remote.dto

data class CotizacionRequestDto(
    val menuSeleccionado: String,
    val cantidadComensales: Int,
    val precioBaseUnitario: Int,
    val incluirTortaPersonalizada: Boolean
)
