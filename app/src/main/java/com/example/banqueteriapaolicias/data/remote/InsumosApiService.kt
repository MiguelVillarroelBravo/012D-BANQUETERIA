package com.example.banqueteriapaolicias.data.remote

import com.example.banqueteriapaolicias.data.remote.dto.PlanificacionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface InsumosApiService {

    @GET("api/insumos/planificacion")
    suspend fun planificar(
        @Query("personas") personas: Int
    ): PlanificacionDto
}