package org.app.saveourpets.datos
import retrofit2.Call
import retrofit2.http.*

interface EspeciesApi {
    @GET("especies")
    fun obtenerEspecies(): Call<List<Especies>>

    @GET("especies/{id}")
    fun obtenerEspeciePorId(@Path("id") id: Int): Call<Especies>
}