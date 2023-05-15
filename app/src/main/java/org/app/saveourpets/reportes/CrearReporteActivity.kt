package org.app.saveourpets.reportes

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
import org.app.saveourpets.usuarios.LoginActivity
import org.app.saveourpets.usuarios.Sesion
import org.app.saveourpets.usuarios.particular.MenuParticularActivity
import org.app.saveourpets.usuarios.particular.MisReportesActivity
import org.app.saveourpets.usuarios.particular.PerfilActivity
import org.app.saveourpets.utils.Validaciones
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class CrearReporteActivity : AppCompatActivity() {
    private lateinit var edtDescripcion : EditText
    private lateinit var edtDireccion : EditText
    private lateinit var btnReportar : Button
    private lateinit var btnVolver : Button
    private lateinit var btnMisReportes : Button
    private lateinit var spinnerIdEspecie : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_reporte)
        agregarReporte()
        accionBtnVolver()
        accionBtnMisReportes()
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
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        baseContext,
                        resources.getString(R.string.error_registros),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    resources.getString(R.string.error_registros),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
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

    private fun errores() : Int {
        var errores : Int = 0
        edtDescripcion = findViewById(R.id.edt_descripcion)
        edtDireccion = findViewById(R.id.edt_direccion)

        if (Validaciones.estaVacio(edtDescripcion.text.toString())) {
            errores += 1
            edtDescripcion.error = resources.getString(R.string.reporte_descripcion_error)
        }
        if (Validaciones.estaVacio(edtDireccion.text.toString())) {
            errores += 1
            edtDireccion.error = resources.getString(R.string.reporte_direccion_error)
        }
        return errores
    }

    private fun agregarReporte() {
        cargarListaDeEspecies()
        btnReportar = findViewById(R.id.btn_reportar)
        btnReportar.setOnClickListener {
            if (errores() == 0) {
                val direccion = edtDireccion.text.toString()
                val descripcion = edtDescripcion.text.toString()
                val especie = spinnerIdEspecie.selectedItem.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    val idEspecie = withContext(Dispatchers.IO) {
                        obtenerIdEspecie(especie)
                    }

                    val reporte = Reporte(0,idEspecie, descripcion,direccion, Sesion.usuario.id_usuario,"",0.0,0.0)
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://saveourpets.probalosv.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    // Crea una instancia del servicio que utiliza la autenticación HTTP básica
                    val api = retrofit.create(ClientAPI::class.java)
                    api.crearReporte(reporte).enqueue(object : Callback<Reporte> {
                        override fun onResponse(call: Call<Reporte>, response: Response<Reporte>) {
                            if (response.isSuccessful) {
                                Toast.makeText(baseContext, resources.getString(R.string.reporte_ok), Toast.LENGTH_SHORT).show()
                            } else {
                                val error = response.errorBody()?.string()
                                Toast.makeText(baseContext, resources.getString(R.string.reporte_error), Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Reporte>, t: Throwable) {
                            Toast.makeText(baseContext, resources.getString(R.string.reporte_error), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private fun accionBtnVolver() {
        btnVolver = findViewById(R.id.btn_volver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MenuParticularActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun accionBtnMisReportes() {
        btnMisReportes = findViewById(R.id.btn_mis_reportes)
        btnMisReportes.setOnClickListener {
            val intent = Intent(this, MisReportesActivity::class.java)
            startActivity(intent)
            finish()
        }
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
                /*R.id.action_adoptar -> {
                    val intent = Intent(this, VacunasActivity::class.java)
                    startActivity(intent)
                    finish()
                }*/
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