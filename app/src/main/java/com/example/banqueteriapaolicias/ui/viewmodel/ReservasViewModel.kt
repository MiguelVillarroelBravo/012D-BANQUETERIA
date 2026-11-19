package com.example.banqueteriapaolicias.ui.viewmodel

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banqueteriapaolicias.data.remote.ReservasRepository
import com.example.banqueteriapaolicias.data.remote.dto.ReservaRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.ReservaResponseDto
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ReservasViewModel : ViewModel() {

    private val repository = ReservasRepository()

    var reservas by mutableStateOf<List<ReservaResponseDto>>(emptyList())
        private set

    var reservaSeleccionada by mutableStateOf<ReservaResponseDto?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    fun limpiarMensajes() {
        errorMessage = null
        successMessage = null
    }

    fun crearReserva(
        request: ReservaRequestDto,
        onSuccess: (ReservaResponseDto) -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading = true
            limpiarMensajes()
            try {
                val response = repository.crearReserva(request)
                if (response.isSuccessful && response.body() != null) {
                    val creada = response.body()!!
                    reservaSeleccionada = creada
                    successMessage = "Reserva creada correctamente"
                    onSuccess(creada)
                } else {
                    errorMessage = "Error al crear reserva (${response.code()})"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.localizedMessage ?: "Intenta nuevamente"}"
            } finally {
                isLoading = false
            }
        }
    }

    fun cargarReservasCliente(email: String) {
        viewModelScope.launch {
            isLoading = true
            limpiarMensajes()
            try {
                val response = repository.obtenerReservasPorCliente(email)
                if (response.isSuccessful) {
                    reservas = response.body().orEmpty()
                } else {
                    errorMessage = "No se pudieron cargar tus reservas (${response.code()})"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.localizedMessage ?: "Intenta nuevamente"}"
            } finally {
                isLoading = false
            }
        }
    }

    fun cargarTodasLasReservas(estado: String? = null, fecha: String? = null) {
        viewModelScope.launch {
            isLoading = true
            limpiarMensajes()
            try {
                val response = repository.listarTodas(estado = estado, fecha = fecha)
                if (response.isSuccessful) {
                    reservas = response.body().orEmpty()
                } else {
                    errorMessage = "No se pudo cargar la agenda (${response.code()})"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.localizedMessage ?: "Intenta nuevamente"}"
            } finally {
                isLoading = false
            }
        }
    }

    fun subirComprobante(
        reservaId: Long,
        bitmap: Bitmap,
        onSuccess: (ReservaResponseDto) -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading = true
            limpiarMensajes()
            try {
                val base64 = bitmapToBase64(bitmap)
                val response = repository.guardarComprobante(reservaId, base64)
                if (response.isSuccessful && response.body() != null) {
                    val actualizada = response.body()!!
                    reservaSeleccionada = actualizada
                    successMessage = "Comprobante enviado para validación"
                    onSuccess(actualizada)
                } else {
                    errorMessage = "Error al subir comprobante (${response.code()})"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.localizedMessage ?: "Intenta nuevamente"}"
            } finally {
                isLoading = false
            }
        }
    }

    fun cambiarEstadoReserva(
        reservaId: Long,
        nuevoEstado: String,
        onSuccess: (ReservaResponseDto) -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading = true
            limpiarMensajes()
            try {
                val response = repository.cambiarEstado(reservaId, nuevoEstado)
                if (response.isSuccessful && response.body() != null) {
                    val actualizada = response.body()!!
                    reservas = reservas.map { if (it.id == reservaId) actualizada else it }
                    if (reservaSeleccionada?.id == reservaId) {
                        reservaSeleccionada = actualizada
                    }
                    successMessage = "Estado actualizado a $nuevoEstado"
                    onSuccess(actualizada)
                } else {
                    errorMessage = "Error al actualizar estado (${response.code()})"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.localizedMessage ?: "Intenta nuevamente"}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}
