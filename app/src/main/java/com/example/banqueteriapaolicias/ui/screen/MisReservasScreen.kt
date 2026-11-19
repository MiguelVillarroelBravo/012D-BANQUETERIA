package com.example.banqueteriapaolicias.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.data.remote.dto.ReservaResponseDto
import com.example.banqueteriapaolicias.data.session.SessionManager
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.viewmodel.ReservasViewModel
import com.example.banqueteriapaolicias.ui.viewmodel.formatearClp

@Composable
fun MisReservasScreen(
    onBack: () -> Unit = {},
    reservasViewModel: ReservasViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val correoCliente = sessionManager.getCorreo().orEmpty()

    LaunchedEffect(correoCliente) {
        if (correoCliente.isNotBlank()) {
            reservasViewModel.cargarReservasCliente(correoCliente)
        }
    }

    Scaffold(
        topBar = { Header(title = "MIS RESERVAS") },
        bottomBar = { Footer(copyrightText = "© 2026 Paolicias.Sweet - Banquetería") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mis Reservas",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    if (correoCliente.isNotBlank()) {
                        Text(
                            text = correoCliente,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                OutlinedButton(
                    onClick = {
                        if (correoCliente.isNotBlank()) {
                            reservasViewModel.cargarReservasCliente(correoCliente)
                        }
                    },
                    enabled = !reservasViewModel.isLoading && correoCliente.isNotBlank()
                ) {
                    Text("Actualizar")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                correoCliente.isBlank() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontró el correo del usuario en la sesión.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                reservasViewModel.isLoading && reservasViewModel.reservas.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                reservasViewModel.errorMessage != null && reservasViewModel.reservas.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = reservasViewModel.errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { reservasViewModel.cargarReservasCliente(correoCliente) }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                reservasViewModel.reservas.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no tienes reservas registradas.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = reservasViewModel.reservas,
                            key = { it.id }
                        ) { reserva ->
                            ReservaClienteCard(reserva = reserva)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
private fun ReservaClienteCard(reserva: ReservaResponseDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    text = "Reserva #${reserva.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reserva.estado,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Evento: ${reserva.tipoEvento}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Fecha: ${reserva.fechaEvento} · Bloque: ${reserva.bloqueHorario}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Comensales: ${reserva.cantidadComensales}",
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Total cotizado: ${formatearClp(reserva.totalCotizacion)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Abono 50%: ${formatearClp(reserva.abonoRequerido)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
