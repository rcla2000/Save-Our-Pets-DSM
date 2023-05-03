package org.app.saveourpets.datos

import org.app.saveourpets.especies.Especie
import org.app.saveourpets.vacunas.Vacuna
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @DELETE("especies/{id}")
    fun eliminarEspecie(@Path("id") id: Int): Call<Especie>

    @GET("vacunas")
    fun getVacunas(): Call<List<Vacuna>>

    @POST("vacunas/crear")
    fun crearVacuna(@Body vacuna: Vacuna) : Call<Vacuna>

    @DELETE("vacunas/{id}")
    fun eliminarVacuna(@Path("id") id: Int): Call<Vacuna>
}