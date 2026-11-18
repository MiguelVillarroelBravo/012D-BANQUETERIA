package com.example.banqueteriapaolicias.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banqueteriapaolicias.data.remote.dto.PlanificacionDto
import com.example.banqueteriapaolicias.ui.components.Footer
import com.example.banqueteriapaolicias.ui.components.Header
import com.example.banqueteriapaolicias.ui.nativos.compartirTexto
import com.example.banqueteriapaolicias.ui.viewmodel.PlanificacionViewModel
import java.util.Locale

private fun cantidad(d: Double): String =
    if (d % 1.0 == 0.0) d.toLong().toString() else d.toString()

private fun clp(n: Long): String =
    "$" + String.format(Locale("es", "CL"), "%,d", n)

private fun listaDeCompras(r: PlanificacionDto): String {
    val faltantes = r.items.filter { it.faltante > 0 }
    val sb = StringBuilder("Lista de compras - evento de ${r.personas} personas\n\n")
    if (faltantes.isEmpty()) {
        sb.append("El stock actual cubre todos los insumos.")
    } else {
        faltantes.forEach {
            sb.append("- ${it.nombre}: ${cantidad(it.faltante)} ${it.unidad} (${clp(it.costoEstimado)})\n")
        }
        sb.append("\nCosto total estimado: ${clp(r.costoTotalEstimado)}")
    }
    return sb.toString()
}

@Composable
fun PlanificacionScreen(
    onBack: () -> Unit = {},
    viewModel: PlanificacionViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

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
            OutlinedTextField(
                value = state.personas,
                onValueChange = viewModel::onPersonasChanged,
                label = { Text("Cantidad de personas del evento") },
                isError = state.errorPersonas != null,
                supportingText = { state.errorPersonas?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::calcular,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular insumos")
            }

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            state.errorApi?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            state.resultado?.let { r ->
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(r.items) { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Requerido: ${cantidad(item.requerido)} ${item.unidad}")
                                Text("Stock: ${cantidad(item.stock)} ${item.unidad}")
                                Text(
                                    "Faltante: ${cantidad(item.faltante)} ${item.unidad}",
                                    color = if (item.faltante > 0) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                Text("Costo estimado: ${clp(item.costoEstimado)}")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Costo total estimado: ${clp(r.costoTotalEstimado)}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { compartirTexto(context, "Lista de compras", listaDeCompras(r)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Compartir lista de compras")
                }
            }

            if (state.resultado == null) {
                Spacer(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Volver")
            }
        }
    }
}