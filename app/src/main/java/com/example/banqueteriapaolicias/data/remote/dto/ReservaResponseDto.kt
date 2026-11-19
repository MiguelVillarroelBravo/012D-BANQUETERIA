package com.example.banqueteriapaolicias.data.remote.dto

data class ReservaResponseDto(
    val id: Long? = null,
    val clienteEmail: String,
    val nombreCliente: String,
    val tipoEvento: String,
    val fechaEvento: String,
    val bloqueHorario: String? = null,
    val cantidadComensales: Int,
    val restriccionesAlimentarias: String? = null,
    val observaciones: String? = null,
    val totalCotizacion: Double,
    val abonoRequerido: Double? = null,
    val comprobanteBase64: String? = null,
    val estado: String? = null,
    val fechaCreacion: String? = null
)
