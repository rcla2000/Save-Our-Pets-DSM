package org.app.saveourpets.usuarios.particular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.mascotas.ListaMascotasAdoptablesActivity
import org.app.saveourpets.reportes.CrearReporteActivity
import org.app.saveourpets.reportes.Reporte
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.usuarios.Sesion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MisReportesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MisReportesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_reportes)
        listarReportes()
    }

    private fun listarReportes() {
        recyclerView = findViewById(R.id.recyclerViewMisReportes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://saveourpets.probalosv.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(ClientAPI::class.java)
        val call = api.getReportesUsuario(Sesion.usuario.id_usuario)
        call.enqueue(object : Callback<List<Reporte>> {
            override fun onResponse(call: Call<List<Reporte>>, response: Response<List<Reporte>>) {
                if (response.isSuccessful) {
                    val reportes = response.body()
                    if (reportes != null) {
                        adapter = MisReportesAdapter(reportes)
                        recyclerView.adapter = adapter
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        baseContext,
                        resources.getString(R.string.error_registros),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Reporte>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    resources.getString(R.string.error_registros),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (Sesion.usuario.id_usuario != 23) {
            menuInflater.inflate(R.menu.menu_usuario, menu)
            return super.onCreateOptionsMenu(menu)
        } else {
            menuInflater.inflate(R.menu.menu_anonimo, menu)
            return super.onCreateOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (Sesion.usuario.id_usuario != 23) {
            when (item.itemId) {
                R.id.action_reportar -> {
                    val intent = Intent(this, CrearReporteActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.action_adoptar -> {
                    val intent = Intent(this, ListaMascotasAdoptablesActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.action_perfil -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.action_cerrar_sesion -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            when (item.itemId) {
                R.id.action_volver -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}