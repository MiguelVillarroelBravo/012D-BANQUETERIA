package com.example.banqueteriapaolicias.model

data class CatalogoModel(
    val servicio1Nombre: String = "Catering Corporativo Premium",
    val servicio1Descripcion: String = "Servicio completo para seminarios y reuniones de empresa. Incluye garzones y mantelería.",
    val servicio1Precio: String = "$85.000",
    val servicio2Nombre: String = "Pack Pastelería Fina (50 unidades)",
    val servicio2Descripcion: String = "Surtido de minipasteles finos artesanales para eventos y celebraciones.",
    val servicio2Precio: String = "$45.000",
    val servicio3Nombre: String = "Torta Personalizada Fondant",
    val servicio3Descripcion: String = "Bizcocho a elección con diseño y modelado en fondant personalizado.",
    val servicio3Precio: String = "$35.000",
    val servicio4Nombre: String = "Banquete Completo (Hasta 100 pers)",
    val servicio4Descripcion: String = "Servicio integral para eventos grandes con menú completo y logística de vajilla.",
    val servicio4Precio: String = "$250.000",
    val servicioSeleccionado: String = "",
    val precioSeleccionado: String = ""
)
