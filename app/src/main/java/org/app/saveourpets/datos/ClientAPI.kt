package org.app.saveourpets.datos

import org.app.saveourpets.especies.Especie
import org.app.saveourpets.mascotas.Mascota
import org.app.saveourpets.vacunas.Vacuna
import org.app.saveourpets.razas.Raza
import org.app.saveourpets.reportes.Reporte
import org.app.saveourpets.usuarios.Usuario
import org.app.saveourpets.usuarios.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClientAPI {

    @POST("login")
    fun login(@Body usuario: Usuario) : Call<Login>

    @POST("usuarios/crear")
    fun crearUsuario(@Body usuario: Usuario) : Call<Usuario>

    @POST("usuarios/actualizar/{id}")
    fun actualizarUsuario(@Path("id") id: Int, @Body usuario: Usuario) : Call<Usuario>

    @GET("razas")
    fun getRazas(): Call<List<Raza>>

    @POST("razas/crear")
    fun crearRaza(@Body raza: Raza) : Call<Raza>

    @POST("razas/actualizar/{id}")
    fun actualizarRaza(@Path("id") id: Int, @Body raza: Raza) : Call<Raza>

    @GET("razas/{id}")
    fun obtenerRazaPorId(@Path("id") id: Int): Call<Raza>

    @DELETE("razas/{id}")
    fun eliminarRaza(@Path("id") id: Int): Call<Raza>

    @GET("reportes")
    fun getReportes(): Call<List<Reporte>>

    @GET("reportes/usuario/{id}")
    fun getReportesUsuario(@Path("id") id: Int): Call<List<Reporte>>

    @POST("reportes/crear")
    fun crearReporte(@Body reporte: Reporte) : Call<Reporte>

    @GET("especies")
    fun getEspecies(): Call<List<Especie>>

    @POST("especies/crear")
    fun crearEspecie(@Body especie: Especie) : Call<Especie>

    @POST("especies/actualizar/{id}")
    fun actualizarEspecie(@Path("id") id: Int, @Body especie: Especie) : Call<Especie>

    @GET("especies/{id}")
    fun obtenerEspeciePorId(@Path("id") id: Int): Call<Especie>

    @GET("especies/id/{nombreEspecie}")
    fun obtenerIdEspecie(@Path("nombreEspecie") nombreEspecie: String): Call<Int>

    @DELETE("especies/{id}")
    fun eliminarEspecie(@Path("id") id: Int): Call<Especie>

    @GET("vacunas")
    fun getVacunas(): Call<List<Vacuna>>

    @POST("vacunas/crear")
    fun crearVacuna(@Body vacuna: Vacuna) : Call<Vacuna>

    @POST("vacunas/actualizar/{id}")
    fun actualizarVacuna(@Path("id") id: Int, @Body vacuna: Vacuna) : Call<Vacuna>

    @DELETE("vacunas/{id}")
    fun eliminarVacuna(@Path("id") id: Int): Call<Vacuna>

    @GET("mascotas")
    fun getMascotas(): Call<List<Mascota>>
}