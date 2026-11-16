package com.example.banqueteriapaolicias.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.viewmodel.AdminCatalogoViewModel
import com.example.banqueteriapaolicias.ui.viewmodel.formatearClp

@Composable
fun AdminCatalogoScreen(
    onBack: () -> Unit = {},
    viewModel: AdminCatalogoViewModel = viewModel()
) {
    val state = viewModel.uiState

    if (state.servicioAEliminar != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onCancelarEliminarServicio() },
            title = { Text("Confirmar eliminación") },
            text = {
                Text(
                    "¿Estás seguro de eliminar el servicio \"${state.servicioAEliminar.nombre}\"? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.onConfirmarEliminarServicio() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onCancelarEliminarServicio() }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = { Header(title = "Gestión de Catálogo") },
        bottomBar = { Footer(copyrightText = "© 2026 Paolicias.Sweet - Banquetería") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.mensajeExito != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = state.mensajeExito,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (state.errorApi != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = state.errorApi,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // 1. Card Formulario de Servicio (Crear / Editar)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (state.servicioEditandoId == null) "Nuevo Servicio" else "Editar Servicio",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    CampoAdminValidado(
                        valor = state.nombre,
                        etiqueta = "Nombre del servicio",
                        error = state.errorNombre,
                        onChange = viewModel::onNombreChanged
                    )

                    CampoAdminValidado(
                        valor = state.descripcion,
                        etiqueta = "Descripción",
                        error = state.errorDescripcion,
                        onChange = viewModel::onDescripcionChanged,
                        singleLine = false
                    )

                    CampoAdminValidado(
                        valor = state.precioUnitario,
                        etiqueta = "Precio unitario ($ CLP)",
                        error = state.errorPrecioUnitario,
                        onChange = viewModel::onPrecioUnitarioChanged,
                        tipoTeclado = KeyboardType.Number
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Activo en catálogo público",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Switch(
                            checked = state.activo,
                            onCheckedChange = viewModel::onActivoChanged
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.onGuardarServicioClick() },
                        enabled = !state.isSaving,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardando...")
                        } else {
                            Text(
                                text = if (state.servicioEditandoId == null) {
                                    "Crear Servicio"
                                } else {
                                    "Guardar Cambios"
                                }
                            )
                        }
                    }

                    if (state.servicioEditandoId != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { viewModel.onCancelarEdicion() },
                            enabled = !state.isSaving,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancelar edición")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Card Tarifario Base (Precio Torta)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tarifario Base (Torta Personalizada)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    CampoAdminValidado(
                        valor = state.precioTorta,
                        etiqueta = "Precio torta por persona ($ CLP)",
                        error = state.errorPrecioTorta,
                        onChange = viewModel::onPrecioTortaChanged,
                        tipoTeclado = KeyboardType.Number
                    )

                    Button(
                        onClick = { viewModel.onActualizarPrecioTortaClick() },
                        enabled = !state.isSaving,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Actualizar Precio Torta")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Listado de Servicios del Catálogo
            Text(
                text = "Servicios Registrados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando catálogo...")
                    }
                }

                state.servicios.isEmpty() -> {
                    Text(
                        text = "No hay servicios registrados.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                else -> {
                    state.servicios.forEach { servicio ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = servicio.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (servicio.activo) "Activo" else "Inactivo",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (servicio.activo) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = servicio.descripcion,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Precio: ${formatearClp(servicio.precioUnitario)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.onSeleccionarParaEditar(servicio) },
                                        enabled = !state.isSaving,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = { viewModel.onSolicitarEliminarServicio(servicio) },
                                        enabled = !state.isSaving,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

@Composable
private fun CampoAdminValidado(
    valor: String,
    etiqueta: String,
    error: String?,
    onChange: (String) -> Unit,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(etiqueta) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 2,
        maxLines = if (singleLine) 1 else 4,
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
