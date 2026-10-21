package com.example.banqueteriapaolicias.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.banqueteriapaolicias.model.ComprobanteModel

class ComprobanteViewModel : ViewModel() {

    var uiState by mutableStateOf(ComprobanteModel())
        private set

    fun onFotoCapturada(nuevaFoto: Bitmap) {
        uiState = uiState.copy(
            foto = nuevaFoto,
            errorFoto = null,
            comprobanteEnviado = false
        )
    }

    fun onEnviarComprobanteClick(onSuccess: () -> Unit) {
        val s = uiState

        val errorFoto = if (s.foto == null) {
            "Debes adjuntar una foto del comprobante de transferencia"
        } else null

        if (errorFoto != null) {
            uiState = s.copy(
                errorFoto = errorFoto,
                comprobanteEnviado = false
            )
            return
        }

        uiState = s.copy(
            estadoReserva = "En validación",
            errorFoto = null,
            comprobanteEnviado = true
        )
        onSuccess()
    }
}
