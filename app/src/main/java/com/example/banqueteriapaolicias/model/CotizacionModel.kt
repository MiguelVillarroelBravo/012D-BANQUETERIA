package com.example.banqueteriapaolicias.model

data class CotizacionModel(
    val menuSeleccionado: String = "Catering Corporativo + Pastelería Fina",
    val cantidadComensales: String = "",
    val precioBaseUnitario: String = "18000",
    val incluirTortaPersonalizada: Boolean = true,
    val errorMenuSeleccionado: String? = null,
    val errorCantidadComensales: String? = null,
    val errorPrecioBaseUnitario: String? = null,
    val tramoAplicado: String = "",
    val porcentajeDescuentoTramo: Int = 0,
    val subtotalMenu: Int = 0,
    val costoTortaPersonalizada: Int = 0,
    val descuentoEscalonado: Int = 0,
    val montoTotalCotizado: Int = 0,
    val montoAbonoCincuentaPorCiento: Int = 0,
    val dotacionEstimada: String = "",
    val cotizacionLista: Boolean = false
)
