package org.app.saveourpets.mascotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.MascotasAdapter
import org.app.saveourpets.mascotas.DetallesMascotaActivity
import org.app.saveourpets.mascotas.Mascota
import org.app.saveourpets.razas.RazasActivity
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.vacunas.VacunasActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaMascotasAdoptablesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MascotasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_mascotas_adoptables)

        listarEspecies()
    }

    private fun listarEspecies() {
        recyclerView = findViewById(R.id.recyclerViewAdoptables)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://saveourpets.probalosv.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(ClientAPI::class.java)
        val call = api.getMascotas()

        call.enqueue(object : Callback<List<Mascota>> {
            override fun onResponse(call: Call<List<Mascota>>, response: Response<List<Mascota>>) {
                if (response.isSuccessful) {
                    val mascot = response.body()
                    if (mascot != null) {
                        val mascotasRescatadas = filtrarMascotasRescatadas(mascot)
                        adapter = MascotasAdapter(mascotasRescatadas)
                        recyclerView.adapter = adapter

                        adapter.setOnItemClickListener(object : MascotasAdapter.OnItemClickListener {
                            override fun onItemClick(mascota1: Mascota) {

                                //"id_mascota", mascota1.id_mascota
                            }
                        })


                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@ListaMascotasAdoptablesActivity,
                        "Error al obtener las Mascotas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Mascota>>, t: Throwable) {
                Toast.makeText(
                    this@ListaMascotasAdoptablesActivity,
                    "Error al obtener las Mascotas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun filtrarMascotasRescatadas(mascotas: List<Mascota>): List<Mascota> {
        return mascotas.filter { it.estado == "Rescatado" || it.id_estado == 1 }
    }

}