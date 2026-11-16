package com.example.banqueteriapaolicias.data.remote.dto

data class TarifarioConfigDto(
    val id: Long = 1L,
    val precioTortaPorPersona: Int,
    val porcentajeAbono: Int = 50
)
