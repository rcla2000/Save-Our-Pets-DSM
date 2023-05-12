package org.app.saveourpets.razas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import kotlinx.coroutines.*
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.Especie
import org.app.saveourpets.especies.ListarEspeciesActivity
import org.app.saveourpets.utils.Validaciones
import org.app.saveourpets.vacunas.VacunasActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class AgregarRazaActivity : AppCompatActivity() {
    private lateinit var razaEditText: EditText
    private lateinit var spinnerIdEspecie : Spinner
    private lateinit var btnAgregar : Button
    private lateinit var btnVolver : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_raza)
        agregarRaza()
    }

    private fun cargarListaDeEspecies() {
        val nombreEspecies = mutableListOf<String>()
        spinnerIdEspecie = findViewById<Spinner>(R.id.spinnerIDEspecie)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.16.102.107/api-save-our-pets/public/api/")
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
                        for (especie in especies) {
                            nombreEspecies.add(especie.nombre)
                        }
                        val adapter = ArrayAdapter<String>(baseContext, android.R.layout.simple_spinner_item, nombreEspecies)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerIdEspecie.adapter = adapter
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@AgregarRazaActivity,
                        resources.getString(R.string.error_registros),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {
                Toast.makeText(
                    this@AgregarRazaActivity,
                    resources.getString(R.string.error_registros),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private suspend fun obtenerIdEspecie(nombreEspecie : String) : Int {
        var idEspecie : Int = 0
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.16.102.107/api-save-our-pets/public/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(ClientAPI::class.java)
        val response = api.obtenerIdEspecie(nombreEspecie).awaitResponse()
        if (response.isSuccessful) {
            idEspecie = response.body().toString().toInt()
        }
        return idEspecie
    }

    private fun guardarRaza() {
        razaEditText = findViewById<EditText>(R.id.edt_raza)
        if (!Validaciones.estaVacio(razaEditText.text.toString())) {
            val raza = razaEditText.text.toString()
            val especie = spinnerIdEspecie.selectedItem.toString()
            // Crear un scope de corutina
            CoroutineScope(Dispatchers.Main).launch {
                val idEspecie = withContext(Dispatchers.IO) {
                    obtenerIdEspecie(especie)
                }
                // Creando objeto raza
                val raza = Raza(0, raza, idEspecie, "", "")
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://172.16.102.107/api-save-our-pets/public/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                val api = retrofit.create(ClientAPI::class.java)
                api.crearRaza(raza).enqueue(object : Callback<Raza> {
                    override fun onResponse(call: Call<Raza>, response: Response<Raza>) {
                        if (response.isSuccessful) {
                            val ok = response.errorBody()?.string()
                            // Se limpian los campos
                            razaEditText.setText("")
                            spinnerIdEspecie.setSelection(0)
                            Toast.makeText(this@AgregarRazaActivity, resources.getString(R.string.info_raza), Toast.LENGTH_SHORT).show()
                        } else {
                            val error = response.errorBody()?.string()
                            Toast.makeText(this@AgregarRazaActivity, resources.getString(R.string.error_raza), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Raza>, t: Throwable) {
                        Toast.makeText(this@AgregarRazaActivity, resources.getString(R.string.error_raza), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            razaEditText.error = resources.getString(R.string.error_raza)
        }
    }

    private fun agregarRaza() {
        btnAgregar = findViewById<Button>(R.id.btn_agregar_raza)
        btnVolver = findViewById<Button>(R.id.btn_volver)
        cargarListaDeEspecies()

        btnAgregar.setOnClickListener {
            guardarRaza()
        }

        btnVolver.setOnClickListener {
            val intent = Intent(this, RazasActivity::class.java)
            startActivity(intent)
            finish()
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