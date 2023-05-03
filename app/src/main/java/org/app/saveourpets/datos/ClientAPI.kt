package org.app.saveourpets.datos

import org.app.saveourpets.especies.Especie
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClientAPI {
    @GET("razas")
    fun getRazas(): Call<List<Raza>>

    @GET("especies")
    fun getEspecies(): Call<List<Especie>>

    @POST("especies/crear")
    fun crearEspecie(@Body especie: Especie) : Call<Especie>

    @POST("especies/actualizar/{id}")
    fun actualizarEspecie(@Path("id") id: Int, @Body especie: Especie) : Call<Especie>

    @GET("especies/{id}")
    fun obtenerEspeciePorId(@Path("id") id: Int): Call<Especie>

    @GET("vacunas")
    fun getVacunas(): Call<List<Vacuna>>
}