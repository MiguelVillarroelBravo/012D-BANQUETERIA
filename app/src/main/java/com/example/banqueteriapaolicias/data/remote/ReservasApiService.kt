package com.example.banqueteriapaolicias.data.remote

import com.example.banqueteriapaolicias.data.remote.dto.ComprobanteRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.EstadoReservaRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.ReservaRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.ReservaResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservasApiService {

    @POST("api/reservas")
    suspend fun crearReserva(
        @Body request: ReservaRequestDto
    ): Response<ReservaResponseDto>

    @GET("api/reservas/cliente/{email}")
    suspend fun obtenerReservasPorCliente(
        @Path("email") email: String
    ): Response<List<ReservaResponseDto>>

    @GET("api/reservas")
    suspend fun listarTodas(
        @Query("estado") estado: String? = null,
        @Query("fecha") fecha: String? = null
    ): Response<List<ReservaResponseDto>>

    @GET("api/reservas/{id}")
    suspend fun obtenerPorId(
        @Path("id") id: Long
    ): Response<ReservaResponseDto>

    @PUT("api/reservas/{id}/comprobante")
    suspend fun guardarComprobante(
        @Path("id") id: Long,
        @Body request: ComprobanteRequestDto
    ): Response<ReservaResponseDto>

    @PATCH("api/reservas/{id}/estado")
    suspend fun cambiarEstado(
        @Path("id") id: Long,
        @Body request: EstadoReservaRequestDto
    ): Response<ReservaResponseDto>
}
