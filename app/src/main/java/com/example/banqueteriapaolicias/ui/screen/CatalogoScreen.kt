package com.example.banqueteriapaolicias.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.components.ProductoCard
import com.example.banqueteriapaolicias.ui.viewmodel.CatalogoViewModel

@Composable
fun CatalogoScreen(
    onNavigateToEvento: () -> Unit = {},
    onBack: () -> Unit = {},
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
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProductoCard(
                nombre = state.servicio1Nombre,
                descripcion = state.servicio1Descripcion,
                precio = state.servicio1Precio,
                onCotizarClick = {
                    viewModel.onCotizarServicioClick(state.servicio1Nombre, state.servicio1Precio) {
                        onNavigateToEvento()
                    }
                }
            )

            ProductoCard(
                nombre = state.servicio2Nombre,
                descripcion = state.servicio2Descripcion,
                precio = state.servicio2Precio,
                onCotizarClick = {
                    viewModel.onCotizarServicioClick(state.servicio2Nombre, state.servicio2Precio) {
                        onNavigateToEvento()
                    }
                }
            )

            ProductoCard(
                nombre = state.servicio3Nombre,
                descripcion = state.servicio3Descripcion,
                precio = state.servicio3Precio,
                onCotizarClick = {
                    viewModel.onCotizarServicioClick(state.servicio3Nombre, state.servicio3Precio) {
                        onNavigateToEvento()
                    }
                }
            )

            ProductoCard(
                nombre = state.servicio4Nombre,
                descripcion = state.servicio4Descripcion,
                precio = state.servicio4Precio,
                onCotizarClick = {
                    viewModel.onCotizarServicioClick(state.servicio4Nombre, state.servicio4Precio) {
                        onNavigateToEvento()
                    }
                }
            )
        }
    }
}
