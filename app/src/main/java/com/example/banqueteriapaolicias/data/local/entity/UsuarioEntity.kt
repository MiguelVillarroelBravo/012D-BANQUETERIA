package com.example.banqueteriapaolicias.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.banqueteriapaolicias.model.Roles

@Entity(tableName = "users")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val clave: String,
    val rol: String = Roles.CLIENTE
)