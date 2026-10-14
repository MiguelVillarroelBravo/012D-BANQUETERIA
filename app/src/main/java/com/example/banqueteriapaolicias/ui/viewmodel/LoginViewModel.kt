package com.example.banqueteriapaolicias.ui.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.banqueteriapaolicias.model.LoginModel

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginModel())
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email, errorEmail = null)
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password, errorPassword = null)
    }

    fun onLoginClick(onSuccess: () -> Unit) {
        val s = uiState

        val errorEmail = if (!Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) "Correo no válido" else null
        val errorPassword = if (s.password.isBlank()) "Ingresa tu contraseña" else null

        uiState = s.copy(errorEmail = errorEmail, errorPassword = errorPassword)

        if (errorEmail == null && errorPassword == null) onSuccess()
    }
}