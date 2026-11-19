package com.example.banqueteriapaolicias.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.data.remote.dto.ReservaRequestDto
import com.example.banqueteriapaolicias.data.session.SessionManager
import com.example.banqueteriapaolicias.ui.components.ComprobanteCamara
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.nativos.vibrar
import com.example.banqueteriapaolicias.ui.viewmodel.ComprobanteViewModel
import com.example.banqueteriapaolicias.ui.viewmodel.ReservaSharedViewModel
import com.example.banqueteriapaolicias.ui.viewmodel.ReservasViewModel

@Composable
fun ComprobanteScreen(
    montoAbonoCincuentaPorCiento: Int? = null,
    onNavigateToAgenda: () -> Unit = {},
    onBack: () -> Unit = {},
    sharedViewModel: ReservaSharedViewModel? = null,
    reservasViewModel: ReservasViewModel = viewModel(),
    viewModel: ComprobanteViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    LaunchedEffect(montoAbonoCincuentaPorCiento) {
        viewModel.actualizarMontoAbonar(montoAbonoCincuentaPorCiento)
    }

    val reservaIdMostrado = reservasViewModel.reservaSeleccionada?.id?.toString() ?: state.numeroReserva
    val estadoMostrado = reservasViewModel.reservaSeleccionada?.estado ?: state.estadoReserva

    Scaffold(
        topBar = {
            Header(title = "Comprobante de Pago")
        },
        bottomBar = {
            Footer(copyrightText = "© 2026 Paolicias.Sweet - Banquetería")
        }
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
                text = "Adjuntar Comprobante",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sube el voucher de transferencia del abono obligatorio (50%)",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Reserva N°: $reservaIdMostrado",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Estado: $estadoMostrado")
                    Text(
                        text = "Monto a abonar: ${state.montoAbonar}",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ComprobanteCamara(
                foto = state.foto,
                onFotoCapturada = viewModel::onFotoCapturada
            )

            if (state.errorFoto != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.errorFoto,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (reservasViewModel.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = reservasViewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val bitmap = state.foto ?: return@Button
                    val correoCliente = sessionManager.getCorreo().orEmpty()
                    val cantidadNum = sharedViewModel?.cantidadAsistentes?.toIntOrNull() ?: 10
                    val totalNum = sharedViewModel?.montoTotalCotizado
                        ?: ((montoAbonoCincuentaPorCiento ?: 0) * 2)
                    val abonoNum = montoAbonoCincuentaPorCiento
                        ?: sharedViewModel?.montoAbonoCincuentaPorCiento
                        ?: 0

                    val request = ReservaRequestDto(
                        clienteEmail = correoCliente,
                        tipoEvento = sharedViewModel?.tipoEvento.orEmpty().ifBlank { "Evento" },
                        fechaEvento = sharedViewModel?.fechaEvento.orEmpty(),
                        bloqueHorario = sharedViewModel?.bloqueHorario.orEmpty(),
                        cantidadComensales = cantidadNum,
                        restriccionesAlimentarias = sharedViewModel?.restriccionesAlimentarias.orEmpty(),
                        observaciones = sharedViewModel?.observacionesLogisticas.orEmpty(),
                        totalCotizacion = totalNum,
                        abonoRequerido = abonoNum
                    )

                    reservasViewModel.crearReserva(request) { reservaCreada ->
                        reservasViewModel.subirComprobante(reservaCreada.id, bitmap) {
                            viewModel.onEnviarComprobanteClick {
                                vibrar(context, 500L)
                                Toast.makeText(
                                    context,
                                    "Comprobante enviado exitosamente.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onNavigateToAgenda()
                            }
                        }
                    }
                },
                enabled = state.foto != null && !reservasViewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (reservasViewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Enviando reserva y comprobante...")
                } else {
                    Text(text = "Enviar Comprobante")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { onBack() },
                enabled = !reservasViewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver")
            }
        }
    }
}
