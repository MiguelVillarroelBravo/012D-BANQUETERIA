package com.example.banqueteriapaolicias.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.ui.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Crear cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            CampoFormulario(
                valor = state.nombre,
                etiqueta = "Nombre completo",
                error = state.errorNombre,
                onChange = viewModel::onNombreChanged
            )
            CampoFormulario(
                valor = state.correo,
                etiqueta = "Correo electrónico",
                error = state.errorCorreo,
                onChange = viewModel::onCorreoChanged,
                tipoTeclado = KeyboardType.Email
            )
            CampoFormulario(
                valor = state.telefono,
                etiqueta = "Teléfono",
                error = state.errorTelefono,
                onChange = viewModel::onTelefonoChanged,
                tipoTeclado = KeyboardType.Phone
            )
            CampoFormulario(
                valor = state.clave,
                etiqueta = "Contraseña",
                error = state.errorClave,
                onChange = viewModel::onClaveChanged,
                tipoTeclado = KeyboardType.Password,
                esClave = true
            )
            CampoFormulario(
                valor = state.confirmarClave,
                etiqueta = "Repetir contraseña",
                error = state.errorConfirmar,
                onChange = viewModel::onConfirmarClaveChanged,
                tipoTeclado = KeyboardType.Password,
                esClave = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.onRegisterClick(context) {
                        Toast.makeText(context, "Registro exitoso. Por favor inicia sesión.", Toast.LENGTH_SHORT).show()
                        onNavigateToLogin()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Registrarme")
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    valor: String,
    etiqueta: String,
    error: String?,
    onChange: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(etiqueta) },
        singleLine = true,
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        trailingIcon = {
            if (error != null) Icon(Icons.Default.Warning, contentDescription = "Error")
        },
        visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
}