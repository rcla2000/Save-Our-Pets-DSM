package org.app.saveourpets.razas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.app.saveourpets.R
import org.app.saveourpets.datos.ClientAPI
import org.app.saveourpets.especies.Especie
import org.app.saveourpets.especies.ListarEspeciesActivity
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.utils.Validaciones
import org.app.saveourpets.vacunas.VacunasActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarRazaActivity : AppCompatActivity() {
    private lateinit var razaEditText: EditText
    private lateinit var spinnerIdEspecie : Spinner
    private lateinit var btnActualizar : Button
    private lateinit var btnVolver : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_raza)
        actualizarRaza()
    }

    private fun cargarListaDeEspecies() {
        val nombreEspecies = mutableListOf<String>()
        spinnerIdEspecie = findViewById<Spinner>(R.id.spinnerIDEspecie)
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
                        for (especie in especies) {
                            nombreEspecies.add(especie.nombre)
                        }
                        val adapter = ArrayAdapter<String>(baseContext, android.R.layout.simple_spinner_item, nombreEspecies)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerIdEspecie.adapter = adapter
                        val raza : Raza = obtenerDatosRaza()
                        seleccionarEspecieDeSpinner(raza.especie, spinnerIdEspecie)
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@ActualizarRazaActivity,
                        resources.getString(R.string.error_registros),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {
                Toast.makeText(
                    this@ActualizarRazaActivity,
                    resources.getString(R.string.error_registros),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun obtenerDatosRaza() : Raza {
        val idRaza : Int = intent.getIntExtra("id_raza", -1)
        val nombre : String = intent.getStringExtra("nombre").toString()
        val idEspecie : Int = intent.getIntExtra("id_especie", -1)
        val especie : String = intent.getStringExtra("especie").toString()
        val imagen : String = intent.getStringExtra("imagen").toString()
        val raza = Raza(idRaza, nombre, idEspecie, especie, imagen)
        return raza
    }

    private fun seleccionarEspecieDeSpinner(valor: String, spinner: Spinner) {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i) == valor) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private suspend fun obtenerIdEspecie(nombreEspecie : String) : Int {
        var idEspecie : Int = 0
        val retrofit = Retrofit.Builder()
            .baseUrl("https://saveourpets.probalosv.com/api/")
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

    private fun actualizarRaza() {
        cargarListaDeEspecies()
        razaEditText = findViewById<EditText>(R.id.edt_raza)
        btnActualizar = findViewById(R.id.btn_actualizar_raza)
        btnVolver = findViewById(R.id.btn_volver)
        val razaActual : Raza = obtenerDatosRaza()
        // Cargando datos en el formulario
        razaEditText.setText(razaActual.nombre)

        btnActualizar.setOnClickListener {
            if (!Validaciones.estaVacio(razaEditText.text.toString())) {
                val nombreRaza : String = razaEditText.text.toString()
                val especie : String = spinnerIdEspecie.selectedItem.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    val idEspecie = withContext(Dispatchers.IO) {
                        obtenerIdEspecie(especie)
                    }
                    val razaActualizada = Raza(razaActual.id_raza, nombreRaza, idEspecie, especie, "")
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://saveourpets.probalosv.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                    val api = retrofit.create(ClientAPI::class.java)
                    api.actualizarRaza(razaActualizada.id_raza, razaActualizada).enqueue(object : Callback<Raza> {
                        override fun onResponse(call: Call<Raza>, response: Response<Raza>) {
                            if (response.isSuccessful) {
                                val ok = response.errorBody()?.string()
                                Toast.makeText(this@ActualizarRazaActivity, resources.getString(R.string.info_actualizar_raza), Toast.LENGTH_SHORT).show()
                            } else {
                                val error = response.errorBody()?.string()
                                Toast.makeText(this@ActualizarRazaActivity, resources.getString(R.string.error_actualizar_raza), Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Raza>, t: Throwable) {
                            Toast.makeText(this@ActualizarRazaActivity, resources.getString(R.string.error_actualizar_raza), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                razaEditText.error = resources.getString(R.string.error_raza)
            }
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
            R.id.action_cerrar_sesion -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}