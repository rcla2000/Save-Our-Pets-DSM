package org.app.saveourpets.datos

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ClientAPI {
    @GET("razas")
    fun getRazas(): Call<List<Raza>>

    @GET("especies")
    fun getEspecies(): Call<List<Especie>>

    @GET("especies/{id}")
    fun obtenerEspeciePorId(@Path("id") id: Int): Call<Especie>

    @GET("vacunas")
    fun getVacunas(): Call<List<Vacuna>>
}