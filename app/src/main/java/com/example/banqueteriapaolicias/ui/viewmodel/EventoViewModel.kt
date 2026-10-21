package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.banqueteriapaolicias.model.EventoModel

class EventoViewModel : ViewModel() {

    var uiState by mutableStateOf(EventoModel())
        private set

    fun onNombreSolicitanteChanged(valor: String) {
        uiState = uiState.copy(nombreSolicitante = valor, errorNombreSolicitante = null)
    }

    fun onEmpresaChanged(valor: String) {
        uiState = uiState.copy(empresaOrganizacion = valor)
    }

    fun onTipoEventoChanged(valor: String) {
        uiState = uiState.copy(tipoEvento = valor, errorTipoEvento = null)
    }

    fun onFechaEventoChanged(valor: String) {
        uiState = uiState.copy(fechaEvento = valor, errorFechaEvento = null)
    }

    fun onBloqueHorarioChanged(valor: String) {
        uiState = uiState.copy(bloqueHorario = valor, errorBloqueHorario = null)
    }

    fun onCantidadAsistentesChanged(valor: String) {
        uiState = uiState.copy(cantidadAsistentes = valor, errorCantidadAsistentes = null)
    }

    fun onRestriccionesChanged(valor: String) {
        uiState = uiState.copy(restriccionesAlimentarias = valor, errorRestricciones = null)
    }

    fun onObservacionesLogisticasChanged(valor: String) {
        uiState = uiState.copy(observacionesLogisticas = valor)
    }

    fun onConfirmarEventoClick(onSuccess: () -> Unit) {
        val s = uiState

        val errorSolicitante = if (s.nombreSolicitante.isBlank()) {
            "Ingresa el nombre del solicitante o representante"
        } else null

        val errorTipo = if (s.tipoEvento.isBlank()) {
            "Indica el tipo de evento (Seminario, Lanzamiento, Social)"
        } else null

        val regexFecha = Regex("""^\d{2}/\d{2}/\d{4}$""")
        val errorFecha = if (!regexFecha.matches(s.fechaEvento)) {
            "Ingresa una fecha válida (DD/MM/AAAA)"
        } else null

        val errorHorario = if (s.bloqueHorario.isBlank()) {
            "Indica el bloque horario (ej. 10:00 - 14:00)"
        } else null

        val asistentes = s.cantidadAsistentes.toIntOrNull()
        val errorAsistentes = when {
            s.cantidadAsistentes.isBlank() -> "Ingresa la cantidad exacta de asistentes"
            asistentes == null || asistentes < 10 -> "El mínimo de atención es de 10 personas"
            asistentes > 100 -> "Capacidad máxima superada (Tope: 100 personas)"
            asistentes % 10 != 0 -> "Los asistentes se cotizan en rangos de 10 en 10 (10, 20... 100)" // NUEVO
            else -> null
        }

        val errorRestricciones = if (s.restriccionesAlimentarias.isBlank()) {
            "Obligatorio: Indica alergias, celíacos, vegetarianos o escribe 'Ninguna'"
        } else null

        uiState = s.copy(
            errorNombreSolicitante = errorSolicitante,
            errorTipoEvento = errorTipo,
            errorFechaEvento = errorFecha,
            errorBloqueHorario = errorHorario,
            errorCantidadAsistentes = errorAsistentes,
            errorRestricciones = errorRestricciones
        )

        val hayErrores = listOf(
            errorSolicitante,
            errorTipo,
            errorFecha,
            errorHorario,
            errorAsistentes,
            errorRestricciones
        ).any { it != null }

        if (!hayErrores) onSuccess()
    }
}