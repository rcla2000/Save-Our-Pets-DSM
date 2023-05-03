package org.app.saveourpets.especies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListarEspeciesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EspecieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_especies)
        val btnAgregar : FloatingActionButton = findViewById<FloatingActionButton>(R.id.btnAgregarEspecie)

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarEspecieActivity::class.java)
            startActivity(intent)
            finish()
        }
        listarEspecies()
    }

    private fun listarEspecies() {
        recyclerView = findViewById(R.id.recyclerViewEspecie)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.4/api-save-our-pets/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(ClientAPI::class.java)
        val call = api.getEspecies()

        call.enqueue(object : Callback<List<Especie>> {
            override fun onResponse(call: Call<List<Especie>>, response: Response<List<Especie>>) {
                if (response.isSuccessful) {
                    val especies = response.body()
                    if (especies != null) {
                        adapter = EspecieAdapter(especies)
                        recyclerView.adapter = adapter

                        adapter.setOnItemClickListener(object : EspecieAdapter.OnItemClickListener {
                            override fun onItemClick(especie: Especie) {
                                val intent = Intent(baseContext, ActualizarEspecieActivity::class.java)

                                // Se pasa la información de la especie
                                intent.putExtra("id_especie", especie.id_especie)
                                intent.putExtra("nombre", especie.nombre)
                                startActivity(intent)
                                finish()
                            }
                        })

                        adapter.setOnBtnEliminarListener(object : EspecieAdapter.OnBtnEliminarListener {
                            override fun onBtnEliminarClick(especie: Especie) {
                                Toast.makeText(baseContext, "Boton eliminar presionado", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener las especies: $error")
                    Toast.makeText(
                        this@ListarEspeciesActivity,
                        "Error al obtener las especies",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {
                Log.e("API", "Error al obtener las especies: ${t.message}")
                Toast.makeText(
                    this@ListarEspeciesActivity,
                    "Error al obtener las especies",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}