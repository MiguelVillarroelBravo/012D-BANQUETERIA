package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banqueteriapaolicias.data.remote.CatalogoApiService
import com.example.banqueteriapaolicias.data.remote.RetrofitClient
import com.example.banqueteriapaolicias.data.remote.dto.ServicioDto
import com.example.banqueteriapaolicias.data.remote.dto.ServicioRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.TarifarioConfigUpdateDto
import com.example.banqueteriapaolicias.model.AdminCatalogoModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class AdminCatalogoViewModel : ViewModel() {

    private val apiService: CatalogoApiService = RetrofitClient.create()

    var uiState by mutableStateOf(AdminCatalogoModel())
        private set

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorApi = null)
            try {
                val servicios = apiService.obtenerServicios(soloActivos = false)
                val config = apiService.obtenerTarifarioConfig()
                uiState = uiState.copy(
                    servicios = servicios,
                    precioTorta = config.precioTortaPorPersona.toString(),
                    errorPrecioTorta = null,
                    isLoading = false,
                    errorApi = null
                )
            } catch (e: HttpException) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorApi = extraerMensajeErrorHttp(e)
                )
            } catch (_: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorApi = "No se pudieron cargar los datos del catálogo. Verifica tu conexión."
                )
            }
        }
    }

    fun onNombreChanged(valor: String) {
        uiState = uiState.copy(nombre = valor, errorNombre = null, mensajeExito = null, errorApi = null)
    }

    fun onDescripcionChanged(valor: String) {
        uiState = uiState.copy(descripcion = valor, errorDescripcion = null, mensajeExito = null, errorApi = null)
    }

    fun onPrecioUnitarioChanged(valor: String) {
        uiState = uiState.copy(precioUnitario = valor, errorPrecioUnitario = null, mensajeExito = null, errorApi = null)
    }

    fun onActivoChanged(activo: Boolean) {
        uiState = uiState.copy(activo = activo, mensajeExito = null, errorApi = null)
    }

    fun onPrecioTortaChanged(valor: String) {
        uiState = uiState.copy(precioTorta = valor, errorPrecioTorta = null, mensajeExito = null, errorApi = null)
    }

    fun onSeleccionarParaEditar(servicio: ServicioDto) {
        uiState = uiState.copy(
            servicioEditandoId = servicio.id,
            nombre = servicio.nombre,
            descripcion = servicio.descripcion,
            precioUnitario = servicio.precioUnitario.toString(),
            activo = servicio.activo,
            errorNombre = null,
            errorDescripcion = null,
            errorPrecioUnitario = null,
            mensajeExito = null,
            errorApi = null
        )
    }

    fun onCancelarEdicion() {
        uiState = uiState.copy(
            servicioEditandoId = null,
            nombre = "",
            descripcion = "",
            precioUnitario = "",
            activo = true,
            errorNombre = null,
            errorDescripcion = null,
            errorPrecioUnitario = null,
            errorApi = null
        )
    }

    fun onSolicitarEliminarServicio(servicio: ServicioDto) {
        uiState = uiState.copy(servicioAEliminar = servicio, mensajeExito = null, errorApi = null)
    }

    fun onCancelarEliminarServicio() {
        uiState = uiState.copy(servicioAEliminar = null)
    }

    fun onGuardarServicioClick() {
        val s = uiState
        val nombreLimpio = s.nombre.trim()
        val descripcionLimpia = s.descripcion.trim()

        val errorNombre = when {
            nombreLimpio.isBlank() -> "El nombre del servicio es obligatorio"
            nombreLimpio.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }

        val errorDescripcion = when {
            descripcionLimpia.isBlank() -> "La descripción del servicio es obligatoria"
            descripcionLimpia.length < 5 -> "La descripción debe tener al menos 5 caracteres"
            else -> null
        }

        val precio = s.precioUnitario.trim().toIntOrNull()
        val errorPrecio = when {
            s.precioUnitario.isBlank() -> "El precio unitario es obligatorio"
            precio == null -> "Ingresa un monto numérico válido"
            precio < 5000 -> "El precio unitario mínimo es $5.000 CLP"
            else -> null
        }

        if (errorNombre != null || errorDescripcion != null || errorPrecio != null) {
            uiState = s.copy(
                errorNombre = errorNombre,
                errorDescripcion = errorDescripcion,
                errorPrecioUnitario = errorPrecio,
                mensajeExito = null
            )
            return
        }

        val request = ServicioRequestDto(
            nombre = nombreLimpio,
            descripcion = descripcionLimpia,
            precioUnitario = precio!!,
            activo = s.activo
        )

        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true, errorApi = null, mensajeExito = null)
            try {
                val esEdicion = s.servicioEditandoId != null
                if (esEdicion) {
                    apiService.actualizarServicio(s.servicioEditandoId!!, request)
                } else {
                    apiService.crearServicio(request)
                }

                val serviciosActualizados = apiService.obtenerServicios(soloActivos = false)
                uiState = uiState.copy(
                    servicios = serviciosActualizados,
                    servicioEditandoId = null,
                    nombre = "",
                    descripcion = "",
                    precioUnitario = "",
                    activo = true,
                    errorNombre = null,
                    errorDescripcion = null,
                    errorPrecioUnitario = null,
                    isSaving = false,
                    mensajeExito = if (esEdicion) {
                        "Servicio actualizado correctamente"
                    } else {
                        "Servicio creado correctamente"
                    }
                )
            } catch (e: HttpException) {
                uiState = uiState.copy(
                    isSaving = false,
                    errorApi = extraerMensajeErrorHttp(e)
                )
            } catch (_: Exception) {
                uiState = uiState.copy(
                    isSaving = false,
                    errorApi = "No se pudo guardar el servicio. Intenta nuevamente."
                )
            }
        }
    }

    fun onConfirmarEliminarServicio() {
        val servicio = uiState.servicioAEliminar ?: return
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true, errorApi = null, mensajeExito = null)
            try {
                val response = apiService.eliminarServicio(servicio.id)
                if (!response.isSuccessful) {
                    throw HttpException(response)
                }
                val serviciosActualizados = apiService.obtenerServicios(soloActivos = false)
                val limpiandoEdicion = uiState.servicioEditandoId == servicio.id
                uiState = uiState.copy(
                    servicios = serviciosActualizados,
                    servicioAEliminar = null,
                    servicioEditandoId = if (limpiandoEdicion) null else uiState.servicioEditandoId,
                    nombre = if (limpiandoEdicion) "" else uiState.nombre,
                    descripcion = if (limpiandoEdicion) "" else uiState.descripcion,
                    precioUnitario = if (limpiandoEdicion) "" else uiState.precioUnitario,
                    activo = if (limpiandoEdicion) true else uiState.activo,
                    isSaving = false,
                    mensajeExito = "Servicio eliminado correctamente"
                )
            } catch (e: HttpException) {
                uiState = uiState.copy(
                    servicioAEliminar = null,
                    isSaving = false,
                    errorApi = extraerMensajeErrorHttp(e)
                )
            } catch (_: Exception) {
                uiState = uiState.copy(
                    servicioAEliminar = null,
                    isSaving = false,
                    errorApi = "No se pudo eliminar el servicio. Intenta nuevamente."
                )
            }
        }
    }

    fun onActualizarPrecioTortaClick() {
        val s = uiState
        val precio = s.precioTorta.trim().toIntOrNull()
        val errorTorta = when {
            s.precioTorta.isBlank() -> "El precio de torta por persona es obligatorio"
            precio == null -> "Ingresa un monto numérico válido"
            precio < 0 -> "El precio no puede ser negativo"
            else -> null
        }

        if (errorTorta != null) {
            uiState = s.copy(errorPrecioTorta = errorTorta, mensajeExito = null)
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true, errorApi = null, mensajeExito = null)
            try {
                val updated = apiService.actualizarTarifarioConfig(
                    TarifarioConfigUpdateDto(precioTortaPorPersona = precio!!)
                )
                uiState = uiState.copy(
                    precioTorta = updated.precioTortaPorPersona.toString(),
                    errorPrecioTorta = null,
                    isSaving = false,
                    mensajeExito = "Precio de torta por persona actualizado correctamente"
                )
            } catch (e: HttpException) {
                uiState = uiState.copy(
                    isSaving = false,
                    errorApi = extraerMensajeErrorHttp(e)
                )
            } catch (_: Exception) {
                uiState = uiState.copy(
                    isSaving = false,
                    errorApi = "No se pudo actualizar el precio de la torta. Intenta nuevamente."
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
