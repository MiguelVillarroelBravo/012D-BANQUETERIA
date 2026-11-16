package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banqueteriapaolicias.data.remote.CatalogoApiService
import com.example.banqueteriapaolicias.data.remote.RetrofitClient
import com.example.banqueteriapaolicias.data.remote.dto.CotizacionRequestDto
import com.example.banqueteriapaolicias.model.CotizacionModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class CotizacionViewModel : ViewModel() {

    private val apiService: CatalogoApiService = RetrofitClient.create()

    var uiState by mutableStateOf(CotizacionModel())
        private set

    fun prellenarDatos(menu: String, precioBase: Int?, comensales: String) {
        uiState = uiState.copy(
            menuSeleccionado = if (menu.isNotBlank()) menu else uiState.menuSeleccionado,
            precioBaseUnitario = precioBase?.toString() ?: uiState.precioBaseUnitario,
            cantidadComensales = if (comensales.isNotBlank()) comensales else uiState.cantidadComensales,
            errorMenuSeleccionado = null,
            errorPrecioBaseUnitario = null,
            errorCantidadComensales = null,
            errorApi = null,
            cotizacionLista = false
        )
    }

    fun onMenuSeleccionadoChanged(valor: String) {
        uiState = uiState.copy(
            menuSeleccionado = valor,
            errorMenuSeleccionado = null,
            errorApi = null,
            cotizacionLista = false
        )
    }

    fun onCantidadComensalesChanged(valor: String) {
        uiState = uiState.copy(
            cantidadComensales = valor,
            errorCantidadComensales = null,
            errorApi = null,
            cotizacionLista = false
        )
    }

    fun onPrecioBaseChanged(valor: String) {
        uiState = uiState.copy(
            precioBaseUnitario = valor,
            errorPrecioBaseUnitario = null,
            errorApi = null,
            cotizacionLista = false
        )
    }

    fun onIncluirTortaChanged(incluir: Boolean) {
        uiState = uiState.copy(
            incluirTortaPersonalizada = incluir,
            errorApi = null,
            cotizacionLista = false
        )
    }

    fun onSeleccionarRangoRapido(rangoPersonas: Int) {
        uiState = uiState.copy(
            cantidadComensales = rangoPersonas.toString(),
            errorCantidadComensales = null,
            errorApi = null,
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
                errorApi = null,
                cotizacionLista = false
            )
            return
        }

        val request = CotizacionRequestDto(
            menuSeleccionado = s.menuSeleccionado.trim(),
            cantidadComensales = comensales!!,
            precioBaseUnitario = precioUnitario!!,
            incluirTortaPersonalizada = s.incluirTortaPersonalizada
        )

        viewModelScope.launch {
            uiState = uiState.copy(
                isCalculating = true,
                errorApi = null,
                cotizacionLista = false
            )
            try {
                val response = apiService.calcularCotizacion(request)
                uiState = uiState.copy(
                    errorMenuSeleccionado = null,
                    errorCantidadComensales = null,
                    errorPrecioBaseUnitario = null,
                    tramoAplicado = response.tramoAplicado,
                    porcentajeDescuentoTramo = response.porcentajeDescuentoTramo,
                    subtotalMenu = response.subtotalMenu,
                    costoTortaPersonalizada = response.costoTortaPersonalizada,
                    descuentoEscalonado = response.descuentoEscalonado,
                    montoTotalCotizado = response.montoTotalCotizado,
                    montoAbonoCincuentaPorCiento = response.montoAbonoCincuentaPorCiento,
                    dotacionEstimada = response.dotacionEstimada,
                    cotizacionLista = true,
                    isCalculating = false,
                    errorApi = null
                )
            } catch (e: HttpException) {
                val mensajeServidor = extraerMensajeErrorHttp(e)
                uiState = uiState.copy(
                    isCalculating = false,
                    cotizacionLista = false,
                    errorApi = mensajeServidor
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isCalculating = false,
                    cotizacionLista = false,
                    errorApi = "No se pudo conectar con el servidor de cotizaciones. Intenta nuevamente."
                )
            }
        }
    }

    private fun extraerMensajeErrorHttp(e: HttpException): String {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val json = JSONObject(errorBody)
                json.optString("message").takeIf { it.isNotBlank() }
                    ?: "Error del servidor (${e.code()})"
            } else {
                "Error del servidor (${e.code()})"
            }
        } catch (_: Exception) {
            "Error del servidor (${e.code()})"
        }
    }
}
