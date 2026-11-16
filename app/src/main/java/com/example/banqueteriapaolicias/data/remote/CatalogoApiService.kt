package com.example.banqueteriapaolicias.data.remote

import com.example.banqueteriapaolicias.data.remote.dto.CotizacionRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.CotizacionResponseDto
import com.example.banqueteriapaolicias.data.remote.dto.ServicioDto
import com.example.banqueteriapaolicias.data.remote.dto.ServicioRequestDto
import com.example.banqueteriapaolicias.data.remote.dto.TarifarioConfigDto
import com.example.banqueteriapaolicias.data.remote.dto.TarifarioConfigUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @POST("api/servicios")
    suspend fun crearServicio(
        @Body request: ServicioRequestDto
    ): ServicioDto

    @PUT("api/servicios/{id}")
    suspend fun actualizarServicio(
        @Path("id") id: Long,
        @Body request: ServicioRequestDto
    ): ServicioDto

    @DELETE("api/servicios/{id}")
    suspend fun eliminarServicio(
        @Path("id") id: Long
    ): Response<Unit>

    @GET("api/tarifario/config")
    suspend fun obtenerTarifarioConfig(): TarifarioConfigDto

    @PUT("api/tarifario/config")
    suspend fun actualizarTarifarioConfig(
        @Body request: TarifarioConfigUpdateDto
    ): TarifarioConfigDto
}
