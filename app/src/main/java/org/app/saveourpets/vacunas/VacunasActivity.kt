package org.app.saveourpets.vacunas

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
import org.app.saveourpets.razas.RazasActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VacunasActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VacunaAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacunas)
        val btnAgregar : FloatingActionButton = findViewById<FloatingActionButton>(R.id.btnAgregarVacuna)

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarVacunaActivity::class.java)
            startActivity(intent)
            finish()
        }
        listarVacunas()
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

    private fun listarVacunas() {
        recyclerView = findViewById(R.id.recyclerViewVacunas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
        val builder = crearDialogo()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.7/api-save-our-pets/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(ClientAPI::class.java)
        progressBar.visibility = View.VISIBLE
        val call = api.getVacunas()

        call.enqueue(object : Callback<List<Vacuna>> {
            override fun onResponse(call: Call<List<Vacuna>>, response: Response<List<Vacuna>>) {
                if (response.isSuccessful) {
                    val vacunas = response.body()
                    if (vacunas != null) {
                        adapter = VacunaAdapter(vacunas)
                        recyclerView.adapter = adapter

                        adapter.setOnBtnDetallesListener(object: VacunaAdapter.OnBtnDetallesListener {
                            override fun onBtnDetallesClick(vacuna: Vacuna) {
                                val intent = Intent(baseContext, DetallesVacunaActivity::class.java)
                                // Se pasa la información de la vacuna a la otra actividad
                                intent.putExtra("id_vacuna", vacuna.id_vacuna)
                                intent.putExtra("vacuna", vacuna.vacuna)
                                intent.putExtra("descripcion", vacuna.descripcion)
                                startActivity(intent)
                                finish()
                            }
                        })

                        adapter.setOnBtnActualizarListener(object : VacunaAdapter.OnBtnActualizarListener {
                            override fun onBtnActualizarClick(vacuna: Vacuna) {
                                val intent = Intent(baseContext, ActualizarVacunaActivity::class.java)
                                // Se pasa la información de la vacuna a la otra actividad
                                intent.putExtra("id_vacuna", vacuna.id_vacuna)
                                intent.putExtra("vacuna", vacuna.vacuna)
                                intent.putExtra("descripcion", vacuna.descripcion)
                                startActivity(intent)
                                finish()
                            }
                        })

                        adapter.setOnBtnEliminarListener(object : VacunaAdapter.OnBtnEliminarListener {
                            override fun onBtnEliminarClick(vacuna: Vacuna) {
                                builder.setPositiveButton(resources.getString(R.string.txt_si)) { dialog, which ->
                                    api.eliminarVacuna(vacuna.id_vacuna).enqueue(object :
                                        Callback<Vacuna> {
                                        override fun onResponse(call: Call<Vacuna>, response: Response<Vacuna>) {
                                            if (response.isSuccessful) {
                                                val ok = response.errorBody()?.string()
                                                Toast.makeText(this@VacunasActivity, resources.getString(R.string.info_eliminar_vacuna), Toast.LENGTH_SHORT).show()
                                                listarVacunas()
                                            } else {
                                                val error = response.errorBody()?.string()
                                                Toast.makeText(this@VacunasActivity, resources.getString(R.string.error_eliminar_vacuna), Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<Vacuna>, t: Throwable) {
                                            Toast.makeText(this@VacunasActivity, resources.getString(R.string.error_eliminar_vacuna), Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }

                                // Mostrando cuadro de dialogo al usuario
                                val dialog = builder.create()
                                dialog.show()
                            }
                        })
                        progressBar.visibility = View.GONE
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@VacunasActivity,
                        resources.getString(R.string.error_registros),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Vacuna>>, t: Throwable) {
                Toast.makeText(
                    this@VacunasActivity,
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