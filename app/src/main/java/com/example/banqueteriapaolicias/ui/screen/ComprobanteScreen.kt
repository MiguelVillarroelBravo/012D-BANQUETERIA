package com.example.banqueteriapaolicias.ui.screen

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.banqueteriapaolicias.ui.components.ComprobanteCamara
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.nativos.vibrar

@Composable
fun ComprobanteScreen(
    onNavigateToAgenda: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var foto by remember { mutableStateOf<Bitmap?>(null) }

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
                        text = "Reserva N°: RES-1042",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Estado: Pendiente de validación")
                    Text(
                        text = "Monto a abonar: el 50% de tu cotización",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ComprobanteCamara(
                foto = foto,
                onFotoCapturada = { foto = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    vibrar(context, 500L)
                    Toast.makeText(
                        context,
                        "Comprobante enviado exitosamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    onNavigateToAgenda()
                },
                enabled = foto != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Enviar Comprobante")
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