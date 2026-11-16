package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.util.Locale

fun formatearClp(monto: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CL"))
    return "$${formatter.format(monto)}"
}

class ReservaSharedViewModel : ViewModel() {

    var servicioNombre by mutableStateOf("")
        private set

    var servicioPrecioUnitario by mutableStateOf<Int?>(null)
        private set

    var cantidadAsistentes by mutableStateOf("")
        private set

    var montoAbonoCincuentaPorCiento by mutableStateOf<Int?>(null)
        private set

    fun guardarServicioSeleccionado(nombre: String, precioUnitario: Int) {
        servicioNombre = nombre
        servicioPrecioUnitario = precioUnitario
    }

    fun guardarCantidadAsistentes(cantidad: String) {
        cantidadAsistentes = cantidad
    }

    fun guardarMontoAbono(montoAbono: Int) {
        montoAbonoCincuentaPorCiento = montoAbono
    }
}
