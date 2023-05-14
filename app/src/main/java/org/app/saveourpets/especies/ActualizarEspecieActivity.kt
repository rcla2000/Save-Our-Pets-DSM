package org.app.saveourpets.especies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.razas.RazasActivity
import org.app.saveourpets.utils.Validaciones
import org.app.saveourpets.vacunas.VacunasActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarEspecieActivity : AppCompatActivity() {
    private lateinit var nombreEditText: EditText
    private lateinit var btnActualizar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_especie)
        actualizarEspecie()
    }

    private fun obtenerDatosEspecie() : Especie {
        val idEspecie : Int = intent.getIntExtra("id_especie", -1)
        val nombre : String = intent.getStringExtra("nombre").toString()
        val especie = Especie(idEspecie, nombre)
        return especie
    }

    private fun actualizarEspecie() {
        nombreEditText = findViewById<EditText>(R.id.edt_nombre)
        btnActualizar = findViewById<Button>(R.id.btn_actualizar_especie)
        val especie : Especie = obtenerDatosEspecie()
        // Asigando datos que vienen de actividad anterior
        nombreEditText.setText(especie.nombre)

        btnActualizar.setOnClickListener {
            if (!Validaciones.estaVacio(nombreEditText.text.toString())) {
                val nombre = nombreEditText.text.toString()
                val especie = Especie(especie.id_especie, nombre)
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.7/api-save-our-pets/public/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)

                api.actualizarEspecie(especie.id_especie, especie).enqueue(object : Callback<Especie> {
                    override fun onResponse(call: Call<Especie>, response: Response<Especie>) {
                        if (response.isSuccessful) {
                            val ok = response.errorBody()?.string()
                            Log.e("API", "OK: $ok")
                            Toast.makeText(this@ActualizarEspecieActivity, resources.getString(R.string.info_actualizar_especie), Toast.LENGTH_SHORT).show()
                        } else {
                            val error = response.errorBody()?.string()
                            Log.e("API", "Error al agregar la especie: $error")
                            Toast.makeText(this@ActualizarEspecieActivity, resources.getString(R.string.error_actualizar_especie), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Especie>, t: Throwable) {
                        Toast.makeText(this@ActualizarEspecieActivity, resources.getString(R.string.error_actualizar_especie), Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                nombreEditText.error = resources.getString(R.string.error_nombre_especie)
            }
        }
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