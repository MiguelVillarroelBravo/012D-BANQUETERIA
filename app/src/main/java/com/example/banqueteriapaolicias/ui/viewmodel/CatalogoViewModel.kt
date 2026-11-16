package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banqueteriapaolicias.data.remote.CatalogoApiService
import com.example.banqueteriapaolicias.data.remote.RetrofitClient
import com.example.banqueteriapaolicias.data.remote.dto.ServicioDto
import com.example.banqueteriapaolicias.model.CatalogoModel
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val apiService: CatalogoApiService = RetrofitClient.create()

    var uiState by mutableStateOf(CatalogoModel())
        private set

    init {
        cargarServicios()
    }

    fun cargarServicios() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val servicios = apiService.obtenerServicios(soloActivos = true)
                uiState = uiState.copy(
                    servicios = servicios,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar el catálogo. Verifica tu conexión e intenta nuevamente."
                )
            }
        }
    }

    fun onCotizarServicioClick(
        servicio: ServicioDto,
        onSuccess: (nombre: String, precioUnitario: Int) -> Unit
    ) {
        uiState = uiState.copy(
            servicioSeleccionado = servicio.nombre,
            precioSeleccionado = servicio.precioUnitario
        )
        onSuccess(servicio.nombre, servicio.precioUnitario)
    }
}
