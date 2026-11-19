package com.example.banqueteriapaolicias.data.remote

import com.example.banqueteriapaolicias.data.remote.ReservasApiService
import com.example.banqueteriapaolicias.data.remote.ReservasRetrofitClient
import com.example.banqueteriapaolicias.data.remote.dto.ComprobanteRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.EstadoReservaRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.ReservaRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.ReservaResponseDto
import retrofit2.Response

class ReservasRepository(
    private val api: ReservasApiService = ReservasRetrofitClient.apiService
) {

    suspend fun crearReserva(request: ReservaRequestDto): Response<ReservaResponseDto> {
        return api.crearReserva(request)
    }

    suspend fun obtenerReservasPorCliente(email: String): Response<List<ReservaResponseDto>> {
        return api.obtenerReservasPorCliente(email)
    }

    suspend fun listarTodas(
        estado: String? = null,
        fecha: String? = null
    ): Response<List<ReservaResponseDto>> {
        return api.listarTodas(estado = estado, fecha = fecha)
    }

    suspend fun obtenerPorId(id: Long): Response<ReservaResponseDto> {
        return api.obtenerPorId(id)
    }

    suspend fun guardarComprobante(
        id: Long,
        comprobanteBase64: String
    ): Response<ReservaResponseDto> {
        return api.guardarComprobante(id, ComprobanteRequestDto(comprobanteBase64))
    }

    suspend fun cambiarEstado(
        id: Long,
        estado: String
    ): Response<ReservaResponseDto> {
        return api.cambiarEstado(id, EstadoReservaRequestDto(estado))
    }
}
