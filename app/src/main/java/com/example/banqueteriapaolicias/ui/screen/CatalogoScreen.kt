package com.example.banqueteriapaolicias.ui.screen

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.components.ProductoCard
import com.example.banqueteriapaolicias.ui.viewmodel.CatalogoViewModel
import com.example.banqueteriapaolicias.ui.viewmodel.formatearClp

@Composable
fun CatalogoScreen(
    onNavigateToEvento: (nombre: String, precioUnitario: Int) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: CatalogoViewModel = viewModel()
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            Header(title = "Catálogo de Servicios")
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nuestros Servicios",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Selecciona un servicio de banquetería o pastelería fina",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Cargando servicios...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.cargarServicios() }) {
                            Text("Reintentar")
                        }
                    }
                }

                state.servicios.isEmpty() -> {
                    Text(
                        text = "No hay servicios disponibles en este momento.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }

                else -> {
                    state.servicios.forEach { servicio ->
                        ProductoCard(
                            nombre = servicio.nombre,
                            descripcion = servicio.descripcion,
                            precio = formatearClp(servicio.precioUnitario),
                            onCotizarClick = {
                                viewModel.onCotizarServicioClick(servicio) { nombre, precioUnitario ->
                                    onNavigateToEvento(nombre, precioUnitario)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
