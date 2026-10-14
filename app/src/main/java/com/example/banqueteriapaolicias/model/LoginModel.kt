package com.example.banqueteriapaolicias.model

data class LoginModel(
    val email: String = "",
    val password: String = "",
    val errorEmail: String? = null,
    val errorPassword: String? = null
)