package com.example.banqueteriapaolicias.ui.screen

import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.viewmodel.EventoViewModel

@Composable
fun EventoScreen(
    onNavigateToCotizacion: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: EventoViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    Scaffold(
        topBar = { Header(title = "RESERVA DE EVENTO") },
        bottomBar = { Footer(copyrightText = "© 2026 Paolicias.Sweet - Banquetería") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Reserva de Evento",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Paolicias.Sweet - Vallenar (Máx. 100 asistentes)",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoFormularioEvento(
                valor = state.nombreSolicitante,
                etiqueta = "Nombre del solicitante o representante",
                error = state.errorNombreSolicitante,
                onChange = viewModel::onNombreSolicitanteChanged
            )
            CampoFormularioEvento(
                valor = state.empresaOrganizacion,
                etiqueta = "Empresa o institución (Opcional si es evento social)",
                error = null,
                onChange = viewModel::onEmpresaChanged
            )
            CampoFormularioEvento(
                valor = state.tipoEvento,
                etiqueta = "Tipo de servicio (Seminario, Lanzamiento, Cumpleaños)",
                error = state.errorTipoEvento,
                onChange = viewModel::onTipoEventoChanged
            )
            CampoFormularioEvento(
                valor = state.fechaEvento,
                etiqueta = "Fecha del evento (DD/MM/AAAA)",
                error = state.errorFechaEvento,
                onChange = viewModel::onFechaEventoChanged
            )
            CampoFormularioEvento(
                valor = state.bloqueHorario,
                etiqueta = "Bloque horario (Ej: 10:00 - 14:00 hrs)",
                error = state.errorBloqueHorario,
                onChange = viewModel::onBloqueHorarioChanged
            )
            CampoFormularioEvento(
                valor = state.cantidadAsistentes,
                etiqueta = "Cantidad exacta de asistentes (10 a 100 personas)",
                error = state.errorCantidadAsistentes,
                onChange = viewModel::onCantidadAsistentesChanged,
                tipoTeclado = KeyboardType.Number
            )
            CampoFormularioEvento(
                valor = state.restriccionesAlimentarias,
                etiqueta = "Restricciones alimentarias (Alergias, celíacos, vegetarianos)",
                error = state.errorRestricciones,
                onChange = viewModel::onRestriccionesChanged
            )

            OutlinedTextField(
                value = state.observacionesLogisticas,
                onValueChange = viewModel::onObservacionesLogisticasChanged,
                label = { Text("Requerimientos logísticos o de pastelería (Opcional)") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onConfirmarEventoClick {
                        Toast.makeText(
                            context,
                            "Datos del evento guardados. Calculando cotización...",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateToCotizacion()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Continuar a Cotización")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver")
            }
        }
    }
}

@Composable
private fun CampoFormularioEvento(
    valor: String,
    etiqueta: String,
    error: String?,
    onChange: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text
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
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    )
    Spacer(modifier = Modifier.height(8.dp))
}