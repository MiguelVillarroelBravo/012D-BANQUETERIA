package com.example.banqueteriapaolicias.model

data class EventoModel(
    val nombreSolicitante: String = "",
    val empresaOrganizacion: String = "",
    val tipoEvento: String = "",
    val fechaEvento: String = "",
    val bloqueHorario: String = "",
    val cantidadAsistentes: String = "",
    val restriccionesAlimentarias: String = "",
    val observacionesLogisticas: String = "",
    val errorNombreSolicitante: String? = null,
    val errorTipoEvento: String? = null,
    val errorFechaEvento: String? = null,
    val errorBloqueHorario: String? = null,
    val errorCantidadAsistentes: String? = null,
    val errorRestricciones: String? = null
)