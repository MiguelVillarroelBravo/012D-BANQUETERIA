package com.example.banqueteriapaolicias.ui.screen

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.data.remote.dto.ReservaResponseDto
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.viewmodel.ReservasViewModel
import com.example.banqueteriapaolicias.ui.viewmodel.formatearClp

private val ESTADOS_FILTRO = listOf(
    "TODAS",
    "PENDIENTE",
    "EN_REVISION",
    "VALIDADA",
    "RECHAZADA"
)

@Composable
fun AdminReservasScreen(
    onBack: () -> Unit = {},
    reservasViewModel: ReservasViewModel = viewModel()
) {
    val context = LocalContext.current
    var estadoSeleccionado by remember { mutableStateOf("TODAS") }
    var filtroFecha by remember { mutableStateOf("") }
    var reservaDetalle by remember { mutableStateOf<ReservaResponseDto?>(null) }

    fun aplicarFiltros() {
        val estadoParam = estadoSeleccionado.takeIf { it != "TODAS" }
        val fechaParam = filtroFecha.trim().takeIf { it.isNotBlank() }
        reservasViewModel.cargarTodasLasReservas(estadoParam, fechaParam)
    }

    LaunchedEffect(Unit) {
        reservasViewModel.cargarTodasLasReservas(null, null)
    }

    // Mantiene sincronizada la vista de detalle si cambia el estado de la reserva
    LaunchedEffect(reservasViewModel.reservas, reservaDetalle?.id) {
        val idActual = reservaDetalle?.id
        if (idActual != null) {
            val actualizada = reservasViewModel.reservas.find { it.id == idActual }
                ?: reservasViewModel.reservaSeleccionada?.takeIf { it.id == idActual }
            if (actualizada != null) {
                reservaDetalle = actualizada
            }
        }
    }

    val agendaOrdenada = remember(reservasViewModel.reservas) {
        reservasViewModel.reservas.sortedWith(
            compareBy<ReservaResponseDto>(
                { claveOrdenFecha(it.fechaEvento) },
                { it.bloqueHorario.orEmpty() },
                { it.id ?: Long.MAX_VALUE }
            )
        )
    }

    Scaffold(
        topBar = { Header(title = "AGENDA ADMINISTRADOR") },
        bottomBar = { Footer(copyrightText = "© 2026 Paolicias.Sweet - Banquetería") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (reservaDetalle != null) {
                DetalleReservaAdminContent(
                    reserva = reservaDetalle!!,
                    isLoading = reservasViewModel.isLoading,
                    errorMessage = reservasViewModel.errorMessage,
                    onValidar = { reserva ->
                        val id = reserva.id ?: return@DetalleReservaAdminContent
                        reservasViewModel.cambiarEstadoReserva(id, "VALIDADA") {
                            Toast.makeText(
                                context,
                                "Pago validado para reserva #$id",
                                Toast.LENGTH_SHORT
                            ).show()
                            aplicarFiltros()
                        }
                    },
                    onRechazar = { reserva ->
                        val id = reserva.id ?: return@DetalleReservaAdminContent
                        reservasViewModel.cambiarEstadoReserva(id, "RECHAZADA") {
                            Toast.makeText(
                                context,
                                "Reserva #$id rechazada",
                                Toast.LENGTH_SHORT
                            ).show()
                            aplicarFiltros()
                        }
                    },
                    onVolverLista = { reservaDetalle = null }
                )
            } else {
                Text(
                    text = "Agenda Cronológica de Reservas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ESTADOS_FILTRO) { estado ->
                        FilterChip(
                            selected = estadoSeleccionado == estado,
                            onClick = {
                                estadoSeleccionado = estado
                                aplicarFiltros()
                            },
                            label = { Text(estado) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = filtroFecha,
                        onValueChange = { filtroFecha = it },
                        label = { Text("Filtrar por fecha (DD/MM/AAAA)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { aplicarFiltros() },
                        enabled = !reservasViewModel.isLoading
                    ) {
                        Text("Filtrar")
                    }
                }

                if (filtroFecha.isNotBlank() || estadoSeleccionado != "TODAS") {
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedButton(
                        onClick = {
                            estadoSeleccionado = "TODAS"
                            filtroFecha = ""
                            reservasViewModel.cargarTodasLasReservas(null, null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpiar filtros")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                when {
                    reservasViewModel.isLoading && agendaOrdenada.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    reservasViewModel.errorMessage != null && agendaOrdenada.isEmpty() -> {
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
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = { aplicarFiltros() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }

                    agendaOrdenada.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay reservas para los filtros seleccionados.",
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
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = agendaOrdenada,
                                key = { it.id ?: "${it.clienteEmail}-${it.fechaEvento}-${it.hashCode()}" }
                            ) { reserva ->
                                AgendaReservaCard(
                                    reserva = reserva,
                                    onClick = {
                                        reservaDetalle = reserva
                                        reserva.id?.let { id ->
                                            reservasViewModel.cargarDetalleReserva(id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Panel")
                }
            }
        }
    }
}

@Composable
private fun AgendaReservaCard(
    reserva: ReservaResponseDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${reserva.fechaEvento} · ${reserva.bloqueHorario ?: "Sin bloque"}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reserva.estado ?: "PENDIENTE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Reserva #${reserva.id ?: "-"} · ${reserva.tipoEvento}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Cliente: ${reserva.nombreCliente} (${reserva.clienteEmail})",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Asistentes: ${reserva.cantidadComensales} · Total: ${formatearClp(reserva.totalCotizacion.toInt())}",
                style = MaterialTheme.typography.bodySmall
            )
            if (!reserva.comprobanteBase64.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Comprobante adjunto disponible",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DetalleReservaAdminContent(
    reserva: ReservaResponseDto,
    isLoading: Boolean,
    errorMessage: String?,
    onValidar: (ReservaResponseDto) -> Unit,
    onRechazar: (ReservaResponseDto) -> Unit,
    onVolverLista: () -> Unit
) {
    val comprobanteBitmap = remember(reserva.comprobanteBase64) {
        decodificarBase64AImageBitmap(reserva.comprobanteBase64)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Detalle de Reserva #${reserva.id ?: "-"}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estado actual: ${reserva.estado ?: "PENDIENTE"}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("Solicitante: ${reserva.nombreCliente}")
                Text("Correo: ${reserva.clienteEmail}")
                Text("Tipo de evento: ${reserva.tipoEvento}")
                Text("Fecha evento: ${reserva.fechaEvento}")
                Text("Bloque horario: ${reserva.bloqueHorario ?: "No especificado"}")
                Text("Asistentes: ${reserva.cantidadComensales}")

                if (!reserva.restriccionesAlimentarias.isNullOrBlank()) {
                    Text("Restricciones: ${reserva.restriccionesAlimentarias}")
                }
                if (!reserva.observaciones.isNullOrBlank()) {
                    Text("Observaciones: ${reserva.observaciones}")
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Total cotizado: ${formatearClp(reserva.totalCotizacion.toInt())}",
                    fontWeight = FontWeight.Bold
                )
                val abonoMostrado = reserva.abonoRequerido?.toInt() ?: (reserva.totalCotizacion.toInt() / 2)
                Text(
                    text = "Abono 50%: ${formatearClp(abonoMostrado)}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Comprobante de Pago",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (comprobanteBitmap != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Image(
                    bitmap = comprobanteBitmap,
                    contentDescription = "Comprobante de pago de la reserva",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(8.dp)
                )
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "No hay imagen de comprobante disponible para esta reserva.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onValidar(reserva) },
                enabled = !isLoading && reserva.id != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("Validar Pago")
            }

            Button(
                onClick = { onRechazar(reserva) },
                enabled = !isLoading && reserva.id != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Rechazar Pago")
            }
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onVolverLista,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a la Agenda")
        }
    }
}

private fun decodificarBase64AImageBitmap(base64Raw: String?): ImageBitmap? {
    if (base64Raw.isNullOrBlank()) return null
    return try {
        val cleanBase64 = base64Raw.substringAfter("base64,", base64Raw).trim()
        val bytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
    } catch (_: Exception) {
        null
    }
}

private fun claveOrdenFecha(fecha: String): String {
    val partes = fecha.trim().split("/")
    return if (partes.size == 3 && partes[2].length == 4) {
        "${partes[2]}-${partes[1].padStart(2, '0')}-${partes[0].padStart(2, '0')}"
    } else {
        fecha
    }
}
