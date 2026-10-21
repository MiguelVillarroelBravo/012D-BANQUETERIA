package com.example.banqueteriapaolicias.ui.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.viewmodel.CotizacionViewModel

@Composable
fun CotizacionScreen(
    onNavigateToComprobante: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: CotizacionViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    Scaffold(
        topBar = { Header(title = "COTIZACIÓN") },
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
                text = "Cotización Automática",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tarifario escalonado Paolicias.Sweet (Rangos de 10 en 10)",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            CampoFormularioCotizacion(
                valor = state.menuSeleccionado,
                etiqueta = "Menú o servicio seleccionado",
                error = state.errorMenuSeleccionado,
                onChange = viewModel::onMenuSeleccionadoChanged
            )

            Text(
                text = "Selección rápida de comensales:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.onSeleccionarRangoRapido(20) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("20 pers.")
                }
                OutlinedButton(
                    onClick = { viewModel.onSeleccionarRangoRapido(50) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("50 pers.")
                }
                OutlinedButton(
                    onClick = { viewModel.onSeleccionarRangoRapido(100) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("100 pers.")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            CampoFormularioCotizacion(
                valor = state.cantidadComensales,
                etiqueta = "Cantidad de comensales (10, 20, 30... máx 100)",
                error = state.errorCantidadComensales,
                onChange = viewModel::onCantidadComensalesChanged,
                tipoTeclado = KeyboardType.Number
            )
            CampoFormularioCotizacion(
                valor = state.precioBaseUnitario,
                etiqueta = "Precio unitario base por persona ($ CLP)",
                error = state.errorPrecioBaseUnitario,
                onChange = viewModel::onPrecioBaseChanged,
                tipoTeclado = KeyboardType.Number
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.incluirTortaPersonalizada,
                    onCheckedChange = viewModel::onIncluirTortaChanged
                )
                Text(text = "Incluir Torta Personalizada Pastelería Fina (+$1.500 p/p)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onCalcularCotizacionClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Calcular Tarifario y Abono 50%")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = state.cotizacionLista) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Desglose de Cotización",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Tramo: ${state.tramoAplicado}")
                        Text(text = "Subtotal catering: $${state.subtotalMenu}")
                        Text(
                            text = "Descuento tramo (${state.porcentajeDescuentoTramo}%): -$${state.descuentoEscalonado}"
                        )
                        Text(text = "Pastelería fina / Torta: $${state.costoTortaPersonalizada}")

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            text = "Monto Total Cotizado: $${state.montoTotalCotizado}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Abono obligatorio (50%) para bloquear fecha: $${state.montoAbonoCincuentaPorCiento}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            text = "Dotación sugerida: ${state.dotacionEstimada}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Redirigiendo para adjuntar comprobante del 50%...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onNavigateToComprobante()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Abonar 50% y Adjuntar Comprobante")
                        }
                    }
                }
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
private fun CampoFormularioCotizacion(
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