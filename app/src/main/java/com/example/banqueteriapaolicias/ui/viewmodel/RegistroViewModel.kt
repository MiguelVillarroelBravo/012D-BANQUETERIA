package com.example.banqueteriapaolicias.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.banqueteriapaolicias.data.database.AppDatabase
import com.example.banqueteriapaolicias.data.local.entity.UsuarioEntity
import com.example.banqueteriapaolicias.model.RegistroModel
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    var uiState by mutableStateOf(RegistroModel())
        private set

    fun onNombreChanged(nombre: String) {
        uiState = uiState.copy(nombre = nombre, errorNombre = null)
    }

    fun onCorreoChanged(correo: String) {
        uiState = uiState.copy(correo = correo, errorCorreo = null)
    }

    fun onTelefonoChanged(telefono: String) {
        uiState = uiState.copy(telefono = telefono, errorTelefono = null)
    }

    fun onClaveChanged(clave: String) {
        uiState = uiState.copy(clave = clave, errorClave = null)
    }

    fun onConfirmarClaveChanged(confirmarClave: String) {
        uiState = uiState.copy(confirmarClave = confirmarClave, errorConfirmar = null)
    }

    fun onRegisterClick(context: Context, onSuccess: () -> Unit) {
        val s = uiState

        val errorNombre = if (s.nombre.isBlank()) "Ingresa tu nombre" else null
        val errorCorreo = if (!Patterns.EMAIL_ADDRESS.matcher(s.correo).matches()) "Correo no válido" else null
        val errorTelefono = if (s.telefono.length != 9 || !s.telefono.all { it.isDigit() }) "Ingresa 9 dígitos" else null
        val errorClave = if (s.clave.length < 6) "Mínimo 6 caracteres" else null
        val errorConfirmar = if (s.clave != s.confirmarClave) "Las contraseñas no coinciden" else null

        uiState = s.copy(
            errorNombre = errorNombre,
            errorCorreo = errorCorreo,
            errorTelefono = errorTelefono,
            errorClave = errorClave,
            errorConfirmar = errorConfirmar
        )

        val hayErrores = listOf(errorNombre, errorCorreo, errorTelefono, errorClave, errorConfirmar)
            .any { it != null }

        if (!hayErrores) {
            viewModelScope.launch {
                val db = com.example.banqueteriapaolicias.data.database.AppDatabase.getDatabase(context)
                val existingUser = db.usuarioDao().getUserByEmail(s.correo)

                if (existingUser != null) {
                    uiState = uiState.copy(errorCorreo = "El correo ya está registrado.")
                } else {
                    val newUser = com.example.banqueteriapaolicias.data.local.entity.UsuarioEntity(
                        nombre = s.nombre,
                        correo = s.correo,
                        telefono = s.telefono,
                        clave = s.clave
                    )
                    db.usuarioDao().registerUser(newUser)
                    onSuccess()
                }
            }
        }
    }
}