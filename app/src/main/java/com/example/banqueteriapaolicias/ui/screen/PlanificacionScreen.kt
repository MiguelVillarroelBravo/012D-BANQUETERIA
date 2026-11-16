package com.example.banqueteriapaolicias.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun PlanificacionScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = { Header(title = "Planificación de Insumos") },
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
                text = "Aquí irá la planificación de insumos",
                style = MaterialTheme.typography.bodyLarge
            )
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Volver")
            }
        }
    }
}