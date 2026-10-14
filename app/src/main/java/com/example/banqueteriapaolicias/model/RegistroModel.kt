package com.example.banqueteriapaolicias.model

data class RegistroModel(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorTelefono: String? = null,
    val errorClave: String? = null,
    val errorConfirmar: String? = null
)