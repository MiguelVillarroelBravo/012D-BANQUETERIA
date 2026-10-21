package com.example.banqueteriapaolicias.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.banqueteriapaolicias.model.CatalogoModel

class CatalogoViewModel : ViewModel() {

    var uiState by mutableStateOf(CatalogoModel())
        private set

    fun onCotizarServicioClick(nombre: String, precio: String, onSuccess: () -> Unit) {
        uiState = uiState.copy(
            servicioSeleccionado = nombre,
            precioSeleccionado = precio
        )
        onSuccess()
    }
}
