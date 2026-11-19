package com.example.banqueteriapaolicias.data.remote.dto

data class ReservaRequestDto(
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
    val comprobanteBase64: String? = null
)
