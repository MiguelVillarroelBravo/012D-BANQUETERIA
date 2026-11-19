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

    var tipoEvento by mutableStateOf("")
        private set

    var fechaEvento by mutableStateOf("")
        private set

    var bloqueHorario by mutableStateOf("")
        private set

    var restriccionesAlimentarias by mutableStateOf("")
        private set

    var observacionesLogisticas by mutableStateOf("")
        private set

    var montoTotalCotizado by mutableStateOf<Int?>(null)
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

    fun guardarDatosEvento(
        tipoEvento: String,
        fechaEvento: String,
        bloqueHorario: String,
        cantidadAsistentes: String,
        restriccionesAlimentarias: String,
        observacionesLogisticas: String
    ) {
        this.tipoEvento = tipoEvento
        this.fechaEvento = fechaEvento
        this.bloqueHorario = bloqueHorario
        this.cantidadAsistentes = cantidadAsistentes
        this.restriccionesAlimentarias = restriccionesAlimentarias
        this.observacionesLogisticas = observacionesLogisticas
    }

    fun guardarTotalesCotizacion(montoTotal: Int, montoAbono: Int) {
        montoTotalCotizado = montoTotal
        montoAbonoCincuentaPorCiento = montoAbono
    }

    fun guardarMontoAbono(montoAbono: Int) {
        montoAbonoCincuentaPorCiento = montoAbono
    }
}
