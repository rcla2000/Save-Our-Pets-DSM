package org.app.saveourpets.razas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.ListarEspeciesActivity
import org.app.saveourpets.vacunas.VacunasActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RazasActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RazaAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_razas)
        val btnAgregar : FloatingActionButton = findViewById<FloatingActionButton>(R.id.btnAgregarRaza)

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarRazaActivity::class.java)
            startActivity(intent)
            finish()
        }
        listarRazas()
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

    private fun listarRazas() {
        recyclerView = findViewById(R.id.recyclerViewRazas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
        val builder = crearDialogo()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://saveourpets.probalosv.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(ClientAPI::class.java)
        progressBar.visibility = View.VISIBLE
        val call = api.getRazas()

        call.enqueue(object : Callback<List<Raza>> {
            override fun onResponse(call: Call<List<Raza>>, response: Response<List<Raza>>) {
                if (response.isSuccessful) {
                    val razas = response.body()
                    if (razas != null) {
                        adapter = RazaAdapter(razas)
                        recyclerView.adapter = adapter

                        adapter.setOnBtnActualizarListener(object : RazaAdapter.OnBtnActualizarListener {
                            override fun onBtnActualizarClick(raza: Raza) {
                                val intent = Intent(baseContext, ActualizarRazaActivity::class.java)

                                // Se pasa la información de la raza
                                intent.putExtra("id_raza", raza.id_raza)
                                intent.putExtra("nombre", raza.nombre)
                                intent.putExtra("id_especie", raza.id_especie)
                                intent.putExtra("especie", raza.especie)
                                intent.putExtra("imagen", raza.imagen)
                                startActivity(intent)
                            }
                        })

                        adapter.setOnBtnEliminarListener(object : RazaAdapter.OnBtnEliminarListener {
                            override fun onBtnEliminarClick(raza: Raza) {
                                builder.setPositiveButton(resources.getString(R.string.txt_si)) { dialog, which ->
                                    api.eliminarRaza(raza.id_raza).enqueue(object :
                                        Callback<Raza> {
                                        override fun onResponse(call: Call<Raza>, response: Response<Raza>) {
                                            if (response.isSuccessful) {
                                                val ok = response.errorBody()?.string()
                                                Toast.makeText(this@RazasActivity, resources.getString(R.string.info_eliminar_raza), Toast.LENGTH_SHORT).show()
                                                listarRazas()
                                            } else {
                                                val error = response.errorBody()?.string()
                                                Toast.makeText(this@RazasActivity, resources.getString(R.string.error_eliminar_raza) + error, Toast.LENGTH_LONG).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<Raza>, t: Throwable) {
                                            Toast.makeText(this@RazasActivity, resources.getString(R.string.error_eliminar_raza) + t.message, Toast.LENGTH_LONG).show()
                                        }
                                    })
                                }

                                // Mostrando cuadro de dialogo al usuario
                                val dialog = builder.create()
                                dialog.show()
                            }
                        })
                    }
                    progressBar.visibility = View.GONE
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@RazasActivity,
                        resources.getString(R.string.error_registros),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Raza>>, t: Throwable) {
                Toast.makeText(
                    this@RazasActivity,
                    resources.getString(R.string.error_registros),
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
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
        }
        return super.onOptionsItemSelected(item)
    }
}