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
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.components.ProductoCard

@Composable
fun CatalogoScreen(
    onNavigateToEvento: () -> Unit = {},
    onBack: () -> Unit = {}
) {
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
                nombre = "Catering Corporativo Premium",
                descripcion = "Servicio completo para seminarios y reuniones de empresa. Incluye garzones y mantelería.",
                precio = "$85.000",
                onCotizarClick = onNavigateToEvento
            )

            ProductoCard(
                nombre = "Pack Pastelería Fina (50 unidades)",
                descripcion = "Surtido de minipasteles finos artesanales para eventos y celebraciones.",
                precio = "$45.000",
                onCotizarClick = onNavigateToEvento
            )

            ProductoCard(
                nombre = "Torta Personalizada Fondant",
                descripcion = "Bizcocho a elección con diseño y modelado en fondant personalizado.",
                precio = "$35.000",
                onCotizarClick = onNavigateToEvento
            )

            ProductoCard(
                nombre = "Banquete Completo (Hasta 100 pers)",
                descripcion = "Servicio integral para eventos grandes con menú completo y logística de vajilla.",
                precio = "$250.000",
                onCotizarClick = onNavigateToEvento
            )
        }
    }
}