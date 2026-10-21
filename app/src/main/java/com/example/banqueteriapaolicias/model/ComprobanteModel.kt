package com.example.banqueteriapaolicias.model

import android.graphics.Bitmap

data class ComprobanteModel(
    val numeroReserva: String = "RES-1042",
    val estadoReserva: String = "Pendiente de validación",
    val montoAbonar: String = "el 50% de tu cotización",
    val foto: Bitmap? = null,
    val errorFoto: String? = null,
    val comprobanteEnviado: Boolean = false
)
