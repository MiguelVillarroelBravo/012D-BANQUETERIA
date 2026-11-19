package com.example.banqueteriapaolicias.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header

@Composable
fun AdminHomeScreen(
    onNavigateToPlanificacion: () -> Unit = {},
    onNavigateToAdminCatalogo: () -> Unit = {},
    onNavigateToReservasAdmin: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        topBar = { Header(title = "Panel Administrador") },
        bottomBar = { Footer(copyrightText = "© 2026 Paolicias.Sweet - Banquetería") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido, administrador",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToReservasAdmin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agenda y Validación de Reservas")
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToAdminCatalogo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestión de Catálogo y Precios")
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToPlanificacion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Planificación de insumos")
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
