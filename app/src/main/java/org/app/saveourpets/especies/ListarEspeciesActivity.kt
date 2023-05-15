package org.app.saveourpets.especies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.razas.RazasActivity
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.vacunas.VacunasActivity
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

    private fun crearDialogo() : AlertDialog.Builder {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.txt_confirmacion))
        builder.setMessage(resources.getString(R.string.pregunta_eliminar))
        builder.setNegativeButton(resources.getString(R.string.txt_no)) { dialog, which ->
            Toast.makeText(this, resources.getString(R.string.txt_cancelado), Toast.LENGTH_SHORT).show()
        }
        return builder
    }

    private fun listarEspecies() {
        recyclerView = findViewById(R.id.recyclerViewEspecie)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val builder = crearDialogo()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://saveourpets.probalosv.com/api/")
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
                                builder.setPositiveButton(resources.getString(R.string.txt_si)) { dialog, which ->
                                    api.eliminarEspecie(especie.id_especie).enqueue(object : Callback<Especie> {
                                        override fun onResponse(call: Call<Especie>, response: Response<Especie>) {
                                            if (response.isSuccessful) {
                                                val ok = response.errorBody()?.string()
                                                Toast.makeText(this@ListarEspeciesActivity, resources.getString(R.string.info_eliminar_especie), Toast.LENGTH_SHORT).show()
                                                listarEspecies()
                                            } else {
                                                val error = response.errorBody()?.string()
                                                Toast.makeText(this@ListarEspeciesActivity, resources.getString(R.string.error_eliminar_especie), Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<Especie>, t: Throwable) {
                                            Toast.makeText(this@ListarEspeciesActivity, resources.getString(R.string.error_eliminar_especie), Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }

                                // Mostrando cuadro de dialogo al usuario
                                val dialog = builder.create()
                                dialog.show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@ListarEspeciesActivity,
                        "Error al obtener las especies",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {
                Toast.makeText(
                    this@ListarEspeciesActivity,
                    "Error al obtener las especies",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_especies -> {
                val intent = Intent(this, ListarEspeciesActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_vacunas -> {
                val intent = Intent(this, VacunasActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_razas -> {
                val intent = Intent(this, RazasActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_cerrar_sesion -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}