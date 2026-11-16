package com.example.banqueteriapaolicias.data.remote.dto

data class CotizacionResponseDto(
    val menuSeleccionado: String,
    val cantidadComensales: Int,
    val precioBaseUnitario: Int,
    val incluirTortaPersonalizada: Boolean,
    val tramoAplicado: String,
    val porcentajeDescuentoTramo: Int,
    val subtotalMenu: Int,
    val costoTortaPersonalizada: Int,
    val descuentoEscalonado: Int,
    val montoTotalCotizado: Int,
    val montoAbonoCincuentaPorCiento: Int,
    val dotacionEstimada: String
)
