package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banqueteriapaolicias.data.remote.InsumosApiService
import com.example.banqueteriapaolicias.data.remote.RetrofitClient
import com.example.banqueteriapaolicias.model.PlanificacionModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class PlanificacionViewModel : ViewModel() {

    private val api: InsumosApiService = RetrofitClient.createInsumos()

    var uiState by mutableStateOf(PlanificacionModel())
        private set

    fun onPersonasChanged(valor: String) {
        uiState = uiState.copy(
            personas = valor.filter { it.isDigit() }.take(5),
            errorPersonas = null,
            errorApi = null
        )
    }

    fun calcular() {
        val n = uiState.personas.toIntOrNull()
        val error = when {
            n == null -> "Ingresa la cantidad de personas"
            n <= 0 -> "Debe ser mayor a 0"
            n > 10000 -> "Máximo 10.000 personas"
            else -> null
        }
        if (error != null || n == null) {
            uiState = uiState.copy(errorPersonas = error)
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorApi = null)
            try {
                val resultado = api.planificar(n)
                uiState = uiState.copy(isLoading = false, resultado = resultado)
            } catch (e: HttpException) {
                uiState = uiState.copy(isLoading = false, errorApi = mensajeHttp(e))
            } catch (_: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorApi = "No se pudo conectar con el servicio de insumos. Verifica tu conexión."
                )
            }
        }
    }

    private fun mensajeHttp(e: HttpException): String {
        return try {
            val cuerpo = e.response()?.errorBody()?.string().orEmpty()
            JSONObject(cuerpo).optString("message").ifBlank { "Error ${e.code()}" }
        } catch (_: Exception) {
            "Error ${e.code()}"
        }
    }
}