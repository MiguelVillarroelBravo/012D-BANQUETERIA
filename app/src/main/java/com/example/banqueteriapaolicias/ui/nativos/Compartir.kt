package com.example.banqueteriapaolicias.ui.nativos

import android.content.Context
import android.content.Intent

fun compartirTexto(context: Context, asunto: String, texto: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, asunto)
        putExtra(Intent.EXTRA_TEXT, texto)
    }
    val chooser = Intent.createChooser(intent, "Compartir lista de compras")
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(chooser)
}