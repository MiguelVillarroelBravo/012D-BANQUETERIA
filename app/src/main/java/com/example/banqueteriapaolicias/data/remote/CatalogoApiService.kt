package com.example.banqueteriapaolicias.data.remote

import com.example.banqueteriapaolicias.data.remote.dto.CotizacionRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.CotizacionResponseDto
import com.example.banqueteriapaolicias.data.remote.dto.ServicioDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CatalogoApiService {

    @GET("api/servicios")
    suspend fun obtenerServicios(
        @Query("soloActivos") soloActivos: Boolean = true
    ): List<ServicioDto>

    @POST("api/cotizaciones/calcular")
    suspend fun calcularCotizacion(
        @Body request: CotizacionRequestDto
    ): CotizacionResponseDto
}
