package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.banqueteriapaolicias.model.CotizacionModel

class CotizacionViewModel : ViewModel() {

    var uiState by mutableStateOf(CotizacionModel())
        private set

    fun onMenuSeleccionadoChanged(valor: String) {
        uiState = uiState.copy(
            menuSeleccionado = valor,
            errorMenuSeleccionado = null,
            cotizacionLista = false
        )
    }

    fun onCantidadComensalesChanged(valor: String) {
        uiState = uiState.copy(
            cantidadComensales = valor,
            errorCantidadComensales = null,
            cotizacionLista = false
        )
    }

    fun onPrecioBaseChanged(valor: String) {
        uiState = uiState.copy(
            precioBaseUnitario = valor,
            errorPrecioBaseUnitario = null,
            cotizacionLista = false
        )
    }

    fun onIncluirTortaChanged(incluir: Boolean) {
        uiState = uiState.copy(
            incluirTortaPersonalizada = incluir,
            cotizacionLista = false
        )
    }

    fun onSeleccionarRangoRapido(rangoPersonas: Int) {
        uiState = uiState.copy(
            cantidadComensales = rangoPersonas.toString(),
            errorCantidadComensales = null,
            cotizacionLista = false
        )
    }

    fun onCalcularCotizacionClick() {
        val s = uiState

        val errorMenu = if (s.menuSeleccionado.isBlank()) {
            "Ingresa el menú o producto del catálogo"
        } else null

        val comensales = s.cantidadComensales.toIntOrNull()
        val errorComensales = when {
            s.cantidadComensales.isBlank() -> "Ingresa la cantidad de comensales"
            comensales == null || comensales < 10 -> "El rango mínimo es de 10 personas"
            comensales > 100 -> "El tope máximo de Paolicias.Sweet es de 100 personas"
            comensales % 10 != 0 -> "El tarifario opera en rangos fijos de 10 en 10 (10, 20... 100)"
            else -> null
        }

        val precioUnitario = s.precioBaseUnitario.toIntOrNull()
        val errorPrecio = when {
            s.precioBaseUnitario.isBlank() -> "Ingresa el precio unitario base"
            precioUnitario == null || precioUnitario < 5000 -> "El precio base mínimo es $5.000 CLP"
            else -> null
        }

        if (errorMenu != null || errorComensales != null || errorPrecio != null) {
            uiState = s.copy(
                errorMenuSeleccionado = errorMenu,
                errorCantidadComensales = errorComensales,
                errorPrecioBaseUnitario = errorPrecio,
                cotizacionLista = false
            )
            return
        }

        val cant = comensales!!
        val base = precioUnitario!!

        // Tarifario escalonado según tramo de asistencia (10 en 10 hasta 100)
        val (tramoTexto, porcentajeDesc) = when {
            cant in 10..20 -> "Tramo Inicial (10 a 20 personas)" to 0
            cant in 30..50 -> "Tramo Medio (30 a 50 personas)" to 5
            cant in 60..80 -> "Tramo Corporativo (60 a 80 personas)" to 10
            else -> "Tramo Máximo (90 a 100 personas)" to 15
        }

        val subtotal = cant * base
        val descuento = (subtotal * porcentajeDesc) / 100
        val valorTorta = if (s.incluirTortaPersonalizada) (cant * 1500) else 0
        val total = (subtotal - descuento) + valorTorta
        val abono50 = total / 2

        // Estimación automática de dotación de personal (Garzones, Cocina, Reposteros)
        val garzones = (cant / 20).coerceAtLeast(1)
        val ayudantesCocina = if (cant >= 50) 2 else 1
        val reposteros = if (s.incluirTortaPersonalizada) 1 else 0
        val dotacion = "$garzones garzón(es), $ayudantesCocina ayudante(s) de cocina" +
                if (reposteros > 0) ", $reposteros repostero(a)" else ""

        uiState = s.copy(
            errorMenuSeleccionado = null,
            errorCantidadComensales = null,
            errorPrecioBaseUnitario = null,
            tramoAplicado = tramoTexto,
            porcentajeDescuentoTramo = porcentajeDesc,
            subtotalMenu = subtotal,
            costoTortaPersonalizada = valorTorta,
            descuentoEscalonado = descuento,
            montoTotalCotizado = total,
            montoAbonoCincuentaPorCiento = abono50,
            dotacionEstimada = dotacion,
            cotizacionLista = true
        )
    }
}